package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.InputNode;
import org.heartfulness.avtc.repository.AgentRepository;
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

    private AgentRepository agentRepository;

    public NodeController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }



    // this method is for the input node API
    // documentation said it was post
    @PostMapping("/inputNode")
    @ResponseBody
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inputNodeRequest(@RequestBody InputNode input) {
        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
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
