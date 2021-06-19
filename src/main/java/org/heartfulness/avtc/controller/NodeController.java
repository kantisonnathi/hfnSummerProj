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
        Set<Department> departments = new HashSet<>();
        Collection<Set<Department>> set = new HashSet<>();
        set.add(departments);
        departments.add(currentDepartment);
        List<Agent> agents = this.agentRepository.findAgentsByDepartmentsInAndStatusEquals(departments, AgentStatus.ONLINE); //list of all possible agents.
        List<String> number = new ArrayList<>();
        number.add("9900213110");
        System.out.println(agents);

        /*if (agents.size() > 3) {
            for (int i = 0; i < 3; i++) {
                number.add(agents.get(i).getContactNumber());
                agents.get(i).setStatus(AgentStatus.QUEUED); //set this particular agent to queued
                this.agentRepository.save(agents.get(i));
            }
        } else if (agents.size() > 0) {
            for (Agent agent : agents) {
                number.add(agent.getContactNumber());
                agent.setStatus(AgentStatus.QUEUED); //set this particular agent to queued
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
            System.out.println("NO CLWS");
            return new ResponseEntity<Object>(entities, HttpStatus.OK);
        }*/

        //List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        /*entity.put("action", "tts");
        entity.put("value", "you are being connected to a community level worker");*/
        entity.put("operation", "dial-numbers");
        JSONObject operationData = new JSONObject();
        operationData.put("data",number);
        operationData.put("dial_method", "serial");
        operationData.put("anon_uuid","60cd93e244d59476");
        entity.put("operation_data",operationData);

        //add whatever parameters you want to add.
        //entities.add(entity);

        System.out.println("sending response");
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }


    @GetMapping("/inCall")
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inCallGet(@RequestBody InCallNode inCallNode) {
        List<JSONObject> entities = new ArrayList<>();
        entities.add(new JSONObject());
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }



    @PostMapping("/inCall")
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inCallWebHook(@RequestBody InCallNode inCallNode) {

        switch (inCallNode.getCall_state()) {
            case 1:
                //this means incoming call to server.
                //which means we need to create a caller object and store it.
                Caller caller = new Caller();
                caller.setContactNumber(inCallNode.getClid());
                Call call = new Call();
                call.setCaller(caller);
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
        entities.add(entity);
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
    public ResponseEntity<?> afterCallPost(@RequestBody AfterCallNode afterCallNode) {
        List<JSONObject> entities = new ArrayList<>();
        entities.add(new JSONObject());
        return new ResponseEntity<Object>(entities,HttpStatus.OK);
    }


}
