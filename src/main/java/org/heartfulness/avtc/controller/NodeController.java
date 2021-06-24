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
        Caller caller = this.callerRepository.findByContactNumber("+91" + input.getClid());
        if (caller == null) {
            caller = new Caller();
            input.setClid("+91" + input.getClid());
            caller.setContactNumber(input.getClid());
            this.callerRepository.save(caller);
        }
        String currNodeID = input.getNode_id();
        Language currLanguage = null;
        if (currNodeID.equals(nodeConfiguration.getEnglishNode())) {
            currLanguage = this.languageRepository.findByName("English");
        } else if (currNodeID.equals(nodeConfiguration.getHindiNode())) {
            currLanguage = this.languageRepository.findByName("Hindi");
        }
        Optional<Service> currService = this.serviceRepository.findById(Long.valueOf(input.getInput()));
        Department currentDepartment = this.departmentRepository.findByServiceAndLanguage(currService.get(), currLanguage);
        List<String> number = new ArrayList<>();
        Long x = currentDepartment.getId();
        List<Agent> agents = new ArrayList<>();
        /*List<Call> calls = this.callRepository.findAllByCallerAndCallStatus(caller, CallStatus.CONNECTED_TO_IVR);
        Call call = calls.get(calls.size()-1);*/
        Call call = this.callRepository.findByUid(input.getUid());
        int i = 1;
        while(agents.isEmpty() && i <= 3) {
             agents = this.agentRepository.getByStatusandDepartment(x, i); //i is level
             i++;
        }
        if (agents.isEmpty()) {
            JSONObject entity = new JSONObject();
            call.setStatus(CallStatus.DISCONNECTED);
            this.callRepository.save(call);
            entity.put("action", "tts");
            entity.put("value", "Sorry, there are no community level workers available at this time, please try again " +
                    "later");
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }
        if (agents.size() >= 3) {
            for (int j = 0; j < 3; j++) {
                Agent tempAgent = agents.get(j);
                tempAgent.setStatus(AgentStatus.QUEUED);
                tempAgent.setLeasedBy(call);
                this.agentRepository.save(tempAgent);
                number.add(agents.get(j).getContactNumber());
            }
        } else if (agents.size() < 3) {
            for (int j = 0; j < agents.size(); j++) {
                Agent tempAgent = agents.get(j);
                tempAgent.setStatus(AgentStatus.QUEUED);
                tempAgent.setLeasedBy(call);
                this.agentRepository.save(tempAgent);
                number.add(agents.get(j).getContactNumber());
            }
        }
        /*List<String> numbers=new ArrayList<>();
        numbers.add("+917338897712");*/
        call.setStatus(CallStatus.AWAITING_CONNECTION_TO_AGENT);
        this.callRepository.save(call);
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

        System.out.println("\n\n\n" + jsonString + "\n\n\n");

        for (int i = 0; i < jsonString.length(); i++) {
            if (jsonString.charAt(i) == '[' && jsonString.charAt(i-1) == '\"') {
                jsonString = jsonString.substring(0,i-1) + jsonString.substring(i);
            }
            if (jsonString.charAt(i) == ']' && jsonString.charAt(i+1) == '\"') {
                jsonString = jsonString.substring(0,i+1) + jsonString.substring(i+2);
            }
            if (jsonString.charAt(i) == '\\') {
                jsonString = jsonString.substring(0,i) + jsonString.substring(i+1);
            }
        }
        Gson gson = new Gson();
        InCallNode inCallNode = gson.fromJson(jsonString, InCallNode.class);
        Caller caller = this.callerRepository.findByContactNumber("+91" + inCallNode.getClid().strip());
        if (caller == null) {
            caller = new Caller();
            caller.setContactNumber("+91" + inCallNode.getClid());
            caller = this.callerRepository.save(caller);
        }
        Call call = this.callRepository.findByUid(inCallNode.getUid());
        Agent agent;
        switch (inCallNode.getCall_state()) {
            case 1:
                //this means incoming call to server.
                //which means we need to create a call object and store it.
                call = new Call();
                call.setCaller(caller);
                call.setUid(inCallNode.getUid());
                caller.addCall(call);
                call.setStatus(CallStatus.CONNECTED_TO_IVR);
                this.callerRepository.save(caller);
                /*call = this.callRepository.save(call);*/
                break;
            case 2:
                //call finished
                call.setStatus(CallStatus.DISCONNECTED);
                agent = call.getAgent();
                agent.setStatus(AgentStatus.ONLINE); //the logger should not be updated here
                //set timestamp.
                call.setEndTime(inCallNode.getCall_time());
                this.agentRepository.save(agent);
                call = this.callRepository.save(call);
                break;
            case 5:
                //dialing agents
                //what do i do here.
                break;
            case 6:
                // agent picked up
                String fullUser = inCallNode.getUsers().get(0);
                String phoneNumber = fullUser.substring(fullUser.length() - 10, fullUser.length());
                call.setStatus(CallStatus.CONNECTED_TO_AGENT);
                agent = this.agentRepository.findByContactNumber("+91" + phoneNumber);
                if (agent == null) {
                    System.out.println("Agent is null, state 6\n\n\n\n");
                    break;
                }
                call.setAgent(agent);
                agent.setStatus(AgentStatus.IN_CALL);
                Set<Agent> leasedAgents = call.getLeasing();
                call.setLeasing(null);
                for (Agent currAgent : leasedAgents) {
                    currAgent.setLeasedBy(null);
                    currAgent.setStatus(AgentStatus.ONLINE);
                    this.agentRepository.save(currAgent);
                }
                this.callRepository.save(call);
                break;
            default:
                System.out.println("rip");

        }
        return new ResponseEntity<Object>("OK", HttpStatus.OK);
    }

    @GetMapping("/afterCall")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> afterCallGet(@RequestBody AfterCallNode afterCallNode) {
        List<JSONObject> entities = new ArrayList<>();
        entities.add(new JSONObject());
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }

    @PostMapping(value = "/afterCall",consumes = {"multipart/form-data"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> afterCallPost(@RequestParam("myoperator") String jsonString) {

        System.out.println("\n\n\n" + jsonString + "\n\n\n");

        List<JSONObject> entities = new ArrayList<>();
        Gson gson=new Gson();
        AfterCallNode afterCallNode=gson.fromJson(jsonString,AfterCallNode.class);
        Call call = this.callRepository.findByUid(afterCallNode.get_pm().get(0).getVl());
        //System.out.println(afterCallNode.get_cl());
        String a=afterCallNode.get_cl();
        Caller caller=callerRepository.findByContactNumber(a);
        call.setCaller(caller);
        call.setLocation(afterCallNode.get_se());
        call.setUrl(afterCallNode.get_fu());
        call.setDuration(afterCallNode.get_dr());
        call.setStatus(CallStatus.DISCONNECTED);
        Agent agent=agentRepository.findById(call.getAgent().getId());
        //Agent agent=agentRepository.findByContactNumber("+917338897712");//Phone number was not caught during afternode testing
        call.setAgent(agent);
        callRepository.save(call);
        entities.add(new JSONObject());
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }


}
