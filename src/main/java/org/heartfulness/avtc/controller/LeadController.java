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
import java.util.*;

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
        return findPaginatedByTeam(1, "id", "asc", modelMap, loggedInAgent);
    }

    @GetMapping("/schedulePage/{pageno}")
    public String findPaginatedByTeam(@PathVariable("pageno") Integer pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir, ModelMap modelMap
                                       ,@ModelAttribute Agent loggedInAgent) {
        int pageSize = 5;
        Team team = loggedInAgent.getTeamManaged();
        Page<ScheduleException> page = scheduleExceptionService.findAllPaginatedByTeamAndDate(Date.valueOf(LocalDate.now().plusDays(1)),team, pageNo, pageSize, sortField, sortDir);
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        List<ScheduleException> list = page.getContent();
        Set<TimeSlot> slotset = team.getTimeSlots();
        List<TimeSlot> slots = new ArrayList<>(slotset);
        slots.sort((timeSlot, t1) -> {
            if (timeSlot.getStartTime().toLocalTime().getHour() > t1.getStartTime().toLocalTime().getHour()) {
                return 1;
            } else if (timeSlot.getStartTime().toLocalTime().getHour() == t1.getStartTime().toLocalTime().getHour()) {
                return 0;
            }
            return -1;
        });
        Set<Agent> agents = team.getAgents();
        List<String> headers = new ArrayList<>();
        headers.add("Agent Name");
        for (TimeSlot slot : slots) {
            String header = slot.getStartTime().toLocalTime().getHour() + " - " + slot.getEndTime().toLocalTime().getHour();
            headers.add(header);
        }
        List<Map<String, String>> rows = new ArrayList<>();
        for (Agent agent : agents) {
            Map<String, String> map = new HashMap<>();
            map.put("Agent Name", agent.getName());
            Set<ScheduleException> scheduleExceptions = agent.getScheduleExceptions();
            Set<TimeSlot> agentSlots = new HashSet<>();
            for (ScheduleException s : scheduleExceptions) {
                if (s.getAccepted()) {
                    agentSlots.add(s.getSlot());
                }
            }
            for (TimeSlot slot : slots) {
                String header = slot.getStartTime().toLocalTime().getHour() + " - " + slot.getEndTime().toLocalTime().getHour();
                if (agentSlots.contains(slot)) {
                    map.put(header, "assigned");
                } else {
                    map.put(header, "free");
                }
            }
            rows.add(map);
        }
        modelMap.put("rows", rows);
        modelMap.put("headers", headers);
        modelMap.put("slots", slots);
        modelMap.put("agents", agents);
        modelMap.put("role", "TEAM_LEAD");
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
