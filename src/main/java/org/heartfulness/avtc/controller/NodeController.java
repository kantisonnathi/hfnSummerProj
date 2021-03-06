package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.service.AfterCallService;
import org.heartfulness.avtc.service.InCallService;
import org.heartfulness.avtc.service.InputNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileWriter;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
@RestController
public class NodeController {


    private final InputNodeService inputNodeService;
    private final InCallService inCallService;
    private final AfterCallService afterCallService;

    Logger logger = LoggerFactory.getLogger(NodeController.class);

    public NodeController(InputNodeService inputNodeService, InCallService inCallService, AfterCallService afterCallService) {
        this.inputNodeService = inputNodeService;
        this.inCallService = inCallService;
        this.afterCallService = afterCallService;
    }

    @PostMapping("/inputNode")
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inputNodeRequest(@RequestBody InputNode input) {
        Long startTime = System.currentTimeMillis();
        ResponseEntity<?> responseEntity =  this.inputNodeService.input(input);
        Long endTime = System.currentTimeMillis();
        logger.info("Time taken by inputNode: " + (endTime - startTime));
        return responseEntity;
    }

    @PostMapping(value = "/inCall", consumes = {"multipart/form-data"})
    @ResponseBody
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> inCallWebHook(@RequestParam("myoperator") String jsonString) {
        this.inCallService.inCall(jsonString);
        return new ResponseEntity<Object>("OK", HttpStatus.OK);
    }

    @PostMapping(value = "/afterCall",consumes = {"multipart/form-data"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> afterCallPost(@RequestParam("myoperator") String jsonString) {
        this.afterCallService.after(jsonString);
        return new ResponseEntity<Object>("OK", HttpStatus.OK);
    }


}
