package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.model.InputNode;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.CallerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class NodeController {

    @Autowired
    NodeConfiguration nodeConfiguration;

    private AgentRepository agentRepository;

    private CallerRepository callerRepository;

    public NodeController(AgentRepository agentRepository,CallerRepository callerRepository) {
        this.agentRepository = agentRepository;
        this.callerRepository = callerRepository;
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


        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();

        if (input.getNode_id().equals(nodeConfiguration.getEnglishNode())) {
            // we have an english node.
            if (input.getInput() == 1) {
                //general info?
            } else if (input.getInput() == 2) {
                //counselling?
                entity.put("action", "tts");
                entity.put("value", "you are being connected to a community level worker");
                entity.put("operation", "dial_numbers");
                JSONObject operationData = new JSONObject();
                ArrayList<String> numbers = new ArrayList<>();
                numbers.add("+917338897712");
                numbers.add("+917338897713");
                operationData.put("data",numbers);
                entity.put("operation_data",operationData);
            }
        } else if (input.getNode_id().equals(nodeConfiguration.getHindiNode())) {
            if (input.getInput() == 1) {
                //general info?
            } else if (input.getInput() == 2) {
                //counselling?
            }
        }

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
