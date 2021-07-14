package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.form.ScheduleMakingForm;
import org.heartfulness.avtc.form.ScheduleTopForm;
import org.heartfulness.avtc.model.Agent;
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
import java.util.HashSet;
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
    public String makeSchedule(ModelMap modelMap) {
        List<ScheduleMakingForm> forms = new ArrayList<>();
        List<Agent> agents = this.agentRepository.findAll();
        for (Agent agent : agents) {
            ScheduleMakingForm scheduleMakingForm = new ScheduleMakingForm(agent);
            forms.add(scheduleMakingForm);
        }
        ScheduleTopForm form = new ScheduleTopForm();
        form.setList(forms);
        modelMap.put("form", form);
        modelMap.put("role", "TEAM_LEAD");
        return "schedule/makeSchedule";
    }

    @PostMapping("/schedule/make")
    public String setSchedule(ScheduleTopForm topForm) {
        System.out.println(topForm); //figure out why this isn't working later
        return "redirect:/success";
    }
}
