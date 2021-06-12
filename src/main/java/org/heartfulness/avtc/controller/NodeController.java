package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@CrossOrigin
@RestController
public class NodeController {

    @Autowired
    NodeConfiguration nodeConfiguration;

    private AgentRepository agentRepository;

    private CallerRepository callerRepository;

    private LanguageRepository languageRepository;

    private ServiceRepository serviceRepository;

    private DepartmentRepository departmentRepository;

    public NodeController(AgentRepository agentRepository,CallerRepository callerRepository, LanguageRepository languageRepository,
                          DepartmentRepository departmentRepository) {
        this.agentRepository = agentRepository;
        this.callerRepository = callerRepository;
        this.languageRepository = languageRepository;
        this.departmentRepository = departmentRepository;
    }

    // this method is for the input node API
    // documentation said it was post
    @PostMapping("/inputNode")
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inputNodeRequest(@RequestBody InputNode input) {
        Caller caller = this.callerRepository.findByAllottedID(input.getClid());
        if (caller == null) {
            caller = new Caller();
            caller.setAllottedID(input.getClid());
            this.callerRepository.save(caller);
        }
        // queried/created caller.
        // will create the call object later.
        // finding the language object from node_id
        String currNodeID = input.getNode_id();
        Language currLanguage = null;
        if (currNodeID.equals(nodeConfiguration.getEnglishNode())) {
            currLanguage = this.languageRepository.findByName("English");
        } else if (currNodeID.equals(nodeConfiguration.getHindiNode())) {
            currLanguage = this.languageRepository.findByName("Hindi");
        }
        Service currService = this.serviceRepository.findById(input.getInput());
        Department currentDepartment = this.departmentRepository.findByServiceAndLanguage(currService, currLanguage);
        HashSet<Department> set = new HashSet<>();
        set.add(currentDepartment);
        List<Agent> agents = this.agentRepository.findAllByDepartmentsOrderByTimestamp(set); //list of all possible agents.
        List<String> number = new ArrayList<>();
        if (agents.size() > 3) {
            for (int i = 0; i < 3; i++) {
                number.add(agents.get(i).getContactNumber());
                agents.get(i).setStatus("Queued"); //set this particular agent to queued
                this.agentRepository.save(agents.get(i));
            }
        } else if (agents.size() > 0) {
            for (Agent agent : agents) {
                number.add(agent.getContactNumber());
                agent.setStatus("Queued"); //set this particular agent to queued
                this.agentRepository.save(agent);
            }
        } else {
            // what to do here ?
            List<JSONObject> entities = new ArrayList<JSONObject>();
            JSONObject entity = new JSONObject();
            entity.put("action", "tts");
            entity.put("value", "no available community level workers, please try again shortly. we are " +
                    "sorry for the convenience caused");
            entities.add(entity);
            return new ResponseEntity<Object>(entities, HttpStatus.OK);
        }

        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        entity.put("action", "tts");
        entity.put("value", "you are being connected to a community level worker");
        entity.put("operation", "dial_numbers");
        JSONObject operationData = new JSONObject();
        operationData.put("data",number);
        entity.put("operation_data",operationData);

        //add whatever parameters you want to add.
        entities.add(entity);
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }


    @GetMapping("/in-call")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inCallGet() {
        List<JSONObject> entities = new ArrayList<>();
        entities.add(new JSONObject());
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }



    @PostMapping("/in-call")
    @ResponseBody
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inCallWebHook(@RequestBody InputNode input) {
        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        //add whatever parameters you want to add.
        entities.add(entity);
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }
}
