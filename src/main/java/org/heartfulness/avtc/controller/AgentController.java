package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.repository.AgentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class AgentController {
    private final AgentRepository agentRepository;

    public AgentController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }


    @GetMapping("/{id}/mark/{status}")
    public String markStatus(@PathVariable("status") String status, @PathVariable("id") Integer id) {
        Agent agent = this.agentRepository.findById(id);
        if (status.equals("online")) {
            //mark user online
            agent.setStatus("online");
            this.agentRepository.save(agent);
            //send api req to my operator
        } else if (status.equals("offline")) {
            //mark user offline
            agent.setStatus("offline");
            this.agentRepository.save(agent);
        }
        return "main/error";
    }

    @GetMapping(path="/check/{contactNumber}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sayHello(@PathVariable("contactNumber")String contactNumber) {
        //Get data from service layer into entityList
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
        List<JSONObject> entities=new ArrayList<JSONObject>();
        JSONObject entity=new JSONObject();
        Agent agent=agentRepository.findByContactNumber(contactNumber);
        entity.put("agent",agent);
        entities.add(entity);
        return new ResponseEntity<Object>(entities,HttpStatus.OK);

    }

    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap) {
        Agent agent = new Agent();
        //agent.setId(1);
        modelMap.put("agent", agent);
        return "main/success";
    }


}
