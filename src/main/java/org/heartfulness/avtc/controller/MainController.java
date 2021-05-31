package org.heartfulness.avtc.controller;


import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.repository.AgentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


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
        return "main/phone-num-auth";
    }

    @PostMapping("/main")
    public String postMain(Agent agent) {
        Agent agent1 = this.agentRepository.findByContactNumber(agent.getContactNumber());
        if (agent1 == null) {
            return "main/error";
        }
        if (agent1.getNew()) {
            //redirect to edit detail page. certain things must be filled
        } else {
            //go to success page.
        }

    }

    @GetMapping("/test")
    public String test() {
        return "main/test";
    }






}
