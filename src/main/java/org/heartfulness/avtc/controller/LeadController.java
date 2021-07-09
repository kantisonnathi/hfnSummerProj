package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lead")
public class LeadController {

    private final AgentService agentService;

    private final SecurityService securityService;

    public LeadController(AgentService agentService, SecurityService securityService) {
        this.agentService = agentService;
        this.securityService = securityService;
    }

    @ModelAttribute
    public Agent loggedInAgent() {
        return this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
    }

    @GetMapping("/schedule/make")
    public String makeSchedule(ModelMap modelMap) {
        return "schedule/makeSchedule";
    }
}
