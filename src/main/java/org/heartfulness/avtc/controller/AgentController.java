package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.AfterCallClasses.CategoryCreationDTO;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            logger.setLogEvent(LogEvent.TURNED_ONLINE);
            agent.setStatus(AgentStatus.ONLINE);
        } else if (status.equals("offline")) {
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

    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap) {
        Agent agent = agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("agent", agent);
        CategoryCreationDTO categoryCreationDTO=new CategoryCreationDTO();
        List<Call> calls = this.callRepository.findAllByAgent(agent);
        // parse through calls and keep unsaved ones at the top
        List<Call> unsavedCalls = new ArrayList<>();
        List<Call> saved = new ArrayList<>();
        for (Call call : calls) {
            if (call.isSaved()) {
                saved.add(call);
            } else {
                unsavedCalls.add(call);
            }
            call.setCategory(CallCategory.ADJUSTMENT_DISORDERS);
        }
        Collections.reverse(unsavedCalls);
        Collections.reverse(saved);
        calls.clear();
        calls.addAll(unsavedCalls);
        calls.addAll(saved);
        categoryCreationDTO.setCallList(calls);
        Schedule schedule = new Schedule();
        Other other = new Other();
        schedule.setAgent(agent);
        /*for(int i = 0; i < calls.size(); i++) {
            calls.get(i).setCategory(CallCategory.ADJUSTMENT_DISORDERS);
            categoryCreationDTO.addCall(calls.get(i));
        }*/
        String endTime = "";
        modelMap.put("end", endTime);
        modelMap.put("role", agent.getRole().toString());
        modelMap.put("other",other);
        schedule.setId(1L);
        modelMap.put("schedule", schedule);
        modelMap.put("calls",categoryCreationDTO);
        //agent is validated
        //    List<Call> calls = this.callRepository.findAllByAgent(agent);
        //  modelMap.put("calls",calls);
        return "main/success";

        // return "main/success";
    }
    @PostMapping("/schedule")
    public String getTime(@ModelAttribute("other") Other other) throws ParseException {
        LocalTime t= LocalTime.now();
        Time time=Time.valueOf(t);
        Schedule schedule=new Schedule();
        schedule.setStartTime(time);
        String x=other.getEndtime();
        Agent agent=agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        schedule.setAgent(agent);
        System.out.println(x);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime endtime = LocalTime.parse(x, formatter);
        Time end=Time.valueOf(endtime);
        schedule.setEndTime(end);
        scheduleRepository.save(schedule);
        return "redirect:/success";
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

    @PostMapping("/editCall")
    public String addDescription(@ModelAttribute("calls") CategoryCreationDTO categoryCreationDTO)
    {

        List<Call> calls=categoryCreationDTO.getCallList();
        for(Call call:calls) {
            Call add=callRepository.findById(call.getId());
            add.setDescription(call.getDescription());
            add.setCategory(call.getCategory());
            callRepository.save(add);
        }
        return "redirect:/success";
    }

}
