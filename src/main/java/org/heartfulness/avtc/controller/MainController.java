package org.heartfulness.avtc.controller;


import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.repository.AgentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
public class MainController {


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
        return "main/NOphone-num-auth";
    }

    @PostMapping("/main")
    public String postMain(Agent agent, Principal principal) {
        String phoneNumber = principal.getName();
        Agent agent1 = this.agentRepository.findByContactNumber(agent.getContactNumber());
        if (agent1 == null) {
            return "main/error";
        }
        if (agent1.validate()) {
            //agent is validated. navigate to success page
            return "redirect:/success";
        }
        //return redirected url for editing details
        return "main/error";
    }

    @GetMapping("/test")
    public String test() {
        return "main/test";
    }






}
