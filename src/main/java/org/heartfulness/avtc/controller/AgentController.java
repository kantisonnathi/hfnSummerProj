package org.heartfulness.avtc.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import net.minidev.json.JSONObject;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.CallRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.security.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class AgentController {

    @Autowired
    SecurityService securityService;


    private final AgentRepository agentRepository;
    private final CallRepository callRepository;

    public AgentController(AgentRepository agentRepository, CallRepository callRepository) {
        this.agentRepository = agentRepository;
        this.callRepository = callRepository;
    }


    @GetMapping("/{id}/mark/{status}")
    public String markStatus(@PathVariable("status") String status, @PathVariable("id") Long id) {
        Agent agent = this.agentRepository.findById(id);
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        if (status.equals("online")) {
            //mark user online
            agent.setStatus("online");
        } else if (status.equals("offline")) {
            //mark user offline
            agent.setStatus("offline");
        }
        agent.setTimestamp(timestamp);
        this.agentRepository.save(agent);
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

    @GetMapping(path="/test/{contactNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnAgentObject(@PathVariable("contactNumber")String contactNumber){
        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        Agent agent=agentRepository.findByContactNumber(contactNumber);
        entity.put("agent",agent);
        entities.add(entity);
        return new ResponseEntity<Object>(entities,HttpStatus.OK);

    }

    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap) {
        Agent agent = agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("agent", agent);
        if (agent.validate()) {
            //agent is validated
            List<Call> calls = this.callRepository.findAllByAgent(agent);
            modelMap.put("calls",calls);
            return "main/success";
        }
        else {
            return "redirect:/addDetails";
            //return "redirect:/addDetails";
        }
    }

    @GetMapping("/m/display")
    public String getManagerPage(ModelMap modelMap) {
        Agent managerAgent =  this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        List<Agent> listOfAllAgents = this.agentRepository.findAll();
        modelMap.put("agents", listOfAllAgents);
        modelMap.put("agent", managerAgent);
        return "main/m-only";
    }

}
