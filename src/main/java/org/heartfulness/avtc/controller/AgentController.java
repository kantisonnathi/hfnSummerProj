package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.CallRepository;
import org.heartfulness.avtc.repository.LoggerRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private final LoggerRepository loggerRepository;
    public AgentController(AgentRepository agentRepository, CallRepository callRepository, LoggerRepository loggerRepository) {
        this.agentRepository = agentRepository;
        this.callRepository = callRepository;
        this.loggerRepository = loggerRepository;
    }


    @GetMapping("/mark/{status}")
    public String markStatus(@PathVariable("status") String status) {
        Agent agent = this.agentRepository.findByContactNumber(this.securityService.getUser().getPhoneNumber());
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Logger logger=new Logger();
        logger.setAgent(agent);
        logger.setTimestamp(timestamp);
        if (status.equals("online")) {
            //mark user online
            agent.setStatus(AgentStatus.ONLINE);
            logger.setLogEvent(LogEvent.TURNED_ONLINE);
        } else if (status.equals("offline")) {
            //mark user offline
            agent.setStatus(AgentStatus.OFFLINE);
            logger.setLogEvent(LogEvent.MANUAL_OFFLINE);
        }
        agent.setTimestamp(timestamp);
        this.agentRepository.save(agent);
        this.loggerRepository.save(logger);
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
        modelMap.put("calls",calls);
            //agent is validated
        //    List<Call> calls = this.callRepository.findAllByAgent(agent);
          //  modelMap.put("calls",calls);
            return "main/success";

       // return "main/success";
    }



}
