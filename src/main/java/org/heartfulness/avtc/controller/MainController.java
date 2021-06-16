package org.heartfulness.avtc.controller;


import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

    @Autowired
    private SecurityService securityService;

    private final AgentRepository agentRepository;

    public MainController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }


    @GetMapping("/")
    public String getlogin(ModelMap modelMap) {
        return "main/mainpage";
    }


    @GetMapping("/main")
    public String getMain(ModelMap modelMap) {
        Agent agent = new Agent();
        modelMap.put("userEntity", agent);
        return "main/phone-num-auth";
    }

    @PostMapping("/main")
    public String postMain(Agent agent) {
        //String phoneNumber = principal.getName();
        Agent agent1 = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        if (agent1 == null) {
            return "main/error";
        }
        if (agent1.validate()) {
            //agent is validated. navigate to success page
            return "redirect:/success";
        }
        //return redirected url for editing details
        return "redirect:/addDetails";
    }



}
