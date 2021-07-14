package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.form.ScheduleMakingForm;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.TimeSlotRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/lead")
public class LeadController {

    //TODO: write code to make a new schedule from the given schedule exceptions.

    private final AgentService agentService;

    private final SecurityService securityService;

    private final AgentRepository agentRepository;

    private final TimeSlotRepository timeSlotRepository;

    public LeadController(AgentRepository agentRepository, AgentService agentService, SecurityService securityService, TimeSlotRepository timeSlotRepository) {
        this.agentService = agentService;
        this.agentRepository = agentRepository;
        this.securityService = securityService;
        this.timeSlotRepository = timeSlotRepository;
    }

    @ModelAttribute
    public Agent loggedInAgent() {
        return this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
    }

    @GetMapping("/schedule/make")
    public String makeSchedule(Agent loggedInAgent, ModelMap modelMap) {
        Team team = loggedInAgent.getTeamManaged();
        ScheduleMakingForm scheduleMakingForm = new ScheduleMakingForm(team);
        modelMap.put("role", "TEAM_LEAD");
        return "schedule/makeSchedule";
    }

    @PostMapping("/schedule/make")
    public String setSchedule() {

        return "redirect:/success";
    }
}
