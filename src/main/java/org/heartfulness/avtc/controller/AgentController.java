package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgentController {

    @Autowired
    SecurityService securityService;

    @Autowired
    NodeConfiguration nodeConfiguration;


    private final AgentRepository agentRepository;
    private final CallRepository callRepository;
    private final TeamRepository teamRepository;
    private final LoggerRepository loggerRepository;
    private final ScheduleRepository scheduleRepository;

    public AgentController(AgentRepository agentRepository, CallRepository callRepository, TeamRepository teamRepository, LoggerRepository loggerRepository, ScheduleRepository scheduleRepository) {
        this.agentRepository = agentRepository;
        this.callRepository = callRepository;
        this.teamRepository = teamRepository;
        this.loggerRepository = loggerRepository;
        this.scheduleRepository = scheduleRepository;
    }


    @GetMapping("/mark/{status}")
    public String markStatus(@PathVariable("status") String status) {
        Agent agent = this.agentRepository.findByContactNumber(this.securityService.getUser().getPhoneNumber());
        LocalDateTime localDateTime = LocalDateTime.now();
        Logger logger=new Logger();
        logger.setAgent(agent);

        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        logger.setTimestamp(timestamp);
        if (status.equals("online")) {
            //mark user online
            logger.setLogEvent(LogEvent.TURNED_ONLINE);
            agent.setStatus(AgentStatus.ONLINE);
        } else if (status.equals("offline")) {
            //mark user offline
            logger.setLogEvent(LogEvent.MANUAL_OFFLINE);
            agent.setStatus(AgentStatus.OFFLINE);
        }
        agent.setTimestamp(timestamp);
        this.loggerRepository.save(logger);
        this.agentRepository.save(agent);
        return "main/error";
    }



    @PostMapping("/check")
    @ResponseBody
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public ResponseEntity<?> createSessionCookie(@RequestBody String contactNumber) {
        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        Agent agent = this.agentRepository.findByContactNumber(contactNumber);
        if (agent == null) {
            entity.put("phoneNumber", "failure");
        } else {
            if (agent.getCertified()) {
                entity.put("phoneNumber", "success");
            } else {
                entity.put("phoneNumber", "failure");
            }
        }
        entities.add(entity);
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }

    @PostMapping("/schedule")
    public String setSchedule(Schedule schedule) {
        Agent agent = this.agentRepository.findByContactNumber(this.securityService.getUser().getPhoneNumber());
        /*Time time = Time.valueOf(end);
        LocalTime timeNow = LocalTime.now();
        Time time1 = Time.valueOf(timeNow);
        schedule.setStartTime(time1);*/
        agent.addSchedule(schedule);
        this.scheduleRepository.save(schedule);
        this.agentRepository.save(agent);
        return "redirect:/success";
    }

    /*@GetMapping(path="/test/{contactNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnAgentObject(@PathVariable("contactNumber")String contactNumber){
        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        Agent agent=agentRepository.findByContactNumber(contactNumber);
        entity.put("agent",agent);
        entities.add(entity);
        return new ResponseEntity<Object>(entities,HttpStatus.OK);

    }*/

    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap) {
        Agent agent = agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("agent", agent);
        System.out.println(nodeConfiguration.getEnglishNode());
        List<Call> calls = this.callRepository.findAllByAgent(agent);
        Schedule schedule = new Schedule();
        schedule.setAgent(agent);
        String endTime = "";
        modelMap.put("end", endTime);
        modelMap.put("schedule", schedule);
        modelMap.put("calls",calls);
            //agent is validated
        //    List<Call> calls = this.callRepository.findAllByAgent(agent);
          //  modelMap.put("calls",calls);
            return "main/success";

       // return "main/success";
    }

    @GetMapping("/team/view")
    public String viewTeam(ModelMap modelMap) {
        Agent loggedInAgent = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber()) ;
        List<Agent> agents = new ArrayList<>();
        agents.add(loggedInAgent);
        Team team = this.teamRepository.findTeamByAgentsIn(agents);
        modelMap.put("team", team);
        return "main/viewTeam";
    }



}
