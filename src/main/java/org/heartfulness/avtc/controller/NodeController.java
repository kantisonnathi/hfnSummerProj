package org.heartfulness.avtc.controller;

import com.google.gson.Gson;
import net.minidev.json.JSONObject;
import org.aspectj.lang.annotation.After;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.AfterCallClasses.AfterCallNode;
import org.heartfulness.avtc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class NodeController {

    @Autowired
    NodeConfiguration nodeConfiguration;

    private final AgentRepository agentRepository;

    private final CallerRepository callerRepository;

    private final LanguageRepository languageRepository;

    private final ServiceRepository serviceRepository;

    private final CallRepository callRepository;

    private final DepartmentRepository departmentRepository;

    public NodeController(AgentRepository agentRepository, CallerRepository callerRepository, LanguageRepository languageRepository,
                          ServiceRepository serviceRepository, CallRepository callRepository, DepartmentRepository departmentRepository) {
        this.agentRepository = agentRepository;
        this.callerRepository = callerRepository;
        this.languageRepository = languageRepository;
        this.serviceRepository = serviceRepository;
        this.callRepository = callRepository;
        this.departmentRepository = departmentRepository;
    }

    // this method is for the input node API
    // documentation said it was post
    @PostMapping("/inputNode")
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inputNodeRequest(@RequestBody InputNode input) {
        System.out.println("Data from my operator: " + input.toString());
        Caller caller = this.callerRepository.findByAllottedID(input.getClid());
        if (caller == null) {
            caller = new Caller();
            input.setClid("+91" + input.getClid());
            caller.setContactNumber(input.getClid());
            this.callerRepository.save(caller);
        }
        System.out.println("the caller is " + caller.toString());
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
        System.out.println("the current language is " + currLanguage.getName());
        //Long id = Long.valueOf(input.getInput());
        Optional<Service> currService = this.serviceRepository.findById(Long.valueOf(input.getInput()));
        Department currentDepartment = this.departmentRepository.findByServiceAndLanguage(currService.get(), currLanguage);
        System.out.println("the current service is " + currService.get().getName());
        List<String> number = new ArrayList<>();
        Long x=currentDepartment.getId();
        List<Agent> agents=new ArrayList<>();
        int i = 1;
        while(agents.isEmpty() && i <= 3) {
             agents = this.agentRepository.getByStatusandDepartment(x, i);
             i++;
        }

        number.add(agents.get(0).getContactNumber());
        JSONObject entity = new JSONObject();
        entity.put("operation", "dial-numbers");
        JSONObject operationData = new JSONObject();
        operationData.put("data",number);
        operationData.put("dial_method", "serial");
        operationData.put("anon_uuid","60cd93e244d59476");
        entity.put("operation_data",operationData);
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @PostMapping(value = "/inCall", consumes = {"multipart/form-data"})
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inCallWebHook(@RequestParam("myoperator") String jsonString) {
        Gson gson = new Gson();
        InCallNode inCallNode = gson.fromJson(jsonString, InCallNode.class);
        Caller caller = this.callerRepository.findByContactNumber("+91" + inCallNode.getClid());
        if (caller == null) {
            caller = new Caller();
            caller.setContactNumber("+91" + inCallNode.getClid());
        }
        switch (inCallNode.getCall_state()) {
            case 1:
                //this means incoming call to server.
                //which means we need to create a caller object and store it.
                Call call = new Call();
                call.setCaller(caller);
                caller.addCall(call);
                call.setStartTime(inCallNode.getCall_time());
                this.callerRepository.save(caller);
                this.callRepository.save(call);
                break;
            case 2:
                //call finished

                break;
            case 3:
                //call initiated from agent to caller
                //this doesn't happen
                break;
            case 4:
                //first party no answer
                //also doesn't happen
                break;
            case 5:
                //dialing agents
                break;
            case 6:
                // agent picked up
                break;
            case 7:
                // caller hung up
                break;
            case 9:
                // no one picked up
                break;
            default:
                System.out.println("rip");
        }


        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        //add whatever parameters you want to add.
        //entities.add(entity);
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }

    @GetMapping("/afterCall")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> afterCallGet(@RequestBody AfterCallNode afterCallNode) {

        List<JSONObject> entities = new ArrayList<>();
        entities.add(new JSONObject());
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }

    @PostMapping("/afterCall")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> afterCallPost(@RequestParam("myoperator") String jsonString) {
        List<JSONObject> entities = new ArrayList<>();
        Gson gson=new Gson();
        AfterCallNode afterCallNode=gson.fromJson(jsonString,AfterCallNode.class);
       Call call=new Call();
        Caller caller=callerRepository.findByContactNumber(afterCallNode.get_cr());
      /*  call.setCaller(caller);
        call.setEndTime(afterCallNode.get_et());
        call.setStartTime(afterCallNode.get_st());
        Agent agent=agentRepository.findByContactNumber(afterCallNode.get_ld().get(0).getRr().get(0).get_ct());
        call.setAgent(agent);
        callRepository.save(call);
        entities.add(new JSONObject());*/
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }


}
