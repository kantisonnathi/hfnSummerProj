package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.form.ScheduleForm;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.TeamRepository;
import org.heartfulness.avtc.repository.TimeSlotRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.ScheduleExceptionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/lead")
public class LeadController {

    //TODO: write code to make a new schedule from the given schedule exceptions.
    //TODO: write schedule exception service

    private final AgentService agentService;
    private final SecurityService securityService;
    private final TeamRepository teamRepository;
    private final AgentRepository agentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ScheduleExceptionService scheduleExceptionService;

    public LeadController(AgentRepository agentRepository, AgentService agentService, SecurityService securityService, TeamRepository teamRepository, TimeSlotRepository timeSlotRepository, ScheduleExceptionService scheduleExceptionService) {
        this.agentService = agentService;
        this.agentRepository = agentRepository;
        this.securityService = securityService;
        this.teamRepository = teamRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.scheduleExceptionService = scheduleExceptionService;
    }

    @ModelAttribute
    public Agent loggedInAgent() {
        return this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
    }

    @GetMapping("/schedule/make")
    public String makeSchedule(Agent loggedInAgent, ModelMap modelMap) {
        Team team = loggedInAgent.getTeamManaged();
        Set<TimeSlot> slots = team.getTimeSlots();
        TimeSlot timeSlot = new TimeSlot();
        Agent agent = new Agent();
        Set<Agent> agents = team.getAgents();
        modelMap.put("slots", slots);
        modelMap.put("agents", agents);
        modelMap.put("role", "TEAM_LEAD");
        return findPaginatedByTeam(1, "id", "asc", team, modelMap);
    }

    @GetMapping("/schedulePage/{pageno}")
    public String findPaginatedByTeam(@PathVariable("pageno") Integer pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir, Team team, ModelMap modelMap) {
        int pageSize = 5;
        Page<ScheduleException> page = scheduleExceptionService.findAllPaginatedByTeamAndDate(Date.valueOf(LocalDate.now().plusDays(1)),team, pageNo, pageSize, sortField, sortDir);
        List<ScheduleException> list = page.getContent();
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelMap.put("list", list);
        ScheduleForm scheduleForm = new ScheduleForm();
        modelMap.put("scheduleForm", scheduleForm);
        return "schedule/makeSchedule";
    }

    @PostMapping("/schedule/new")
    public String newException(ScheduleForm scheduleForm) {
        Date date = Date.valueOf(LocalDate.now().plusDays(1));
        Agent agent = scheduleForm.getAgent();
        TimeSlot timeSlot = scheduleForm.getSlot();
        ScheduleException scheduleException = new ScheduleException();
        scheduleException.setSlot(timeSlot);
        scheduleException.setDate(date);
        scheduleException.setAgent(agent);
        scheduleException.setAccepted(true);
        if (this.scheduleExceptionService.findEqual(scheduleException)) {
            this.scheduleExceptionService.save(scheduleException);
        }
        return "redirect:/lead/schedule/make";
    }

    @PostMapping("/schedule/make")
    public String setSchedule() {
        return "redirect:/success";
    }

    @GetMapping("/{exceptionId}/reject")
    public String rejectException(@PathVariable("exceptionId") Long exceptionId) {
        ScheduleException scheduleException = this.scheduleExceptionService.findById(exceptionId);
        scheduleException.setAccepted(false);
        this.scheduleExceptionService.save(scheduleException);
        return "redirect:/lead/schedule/make";
    }

    @GetMapping("/{exceptionId}/accept")
    public String acceptException(@PathVariable("exceptionId") Long exceptionId) {
        ScheduleException scheduleException = this.scheduleExceptionService.findById(exceptionId);
        scheduleException.setAccepted(true);
        this.scheduleExceptionService.save(scheduleException);
        return "redirect:/lead/schedule/make";
    }
}
