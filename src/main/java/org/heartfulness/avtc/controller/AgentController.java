package org.heartfulness.avtc.controller;

import lombok.SneakyThrows;
import org.heartfulness.avtc.form.SlotForm;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.AfterCallClasses.CategoryCreationDTO;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.heartfulness.avtc.model.enums.CallCategory;
import org.heartfulness.avtc.model.enums.LogEvent;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgentController {

    private final SecurityService securityService;

    private final CallService callService;

    private final AgentService agentService;

    private final ScheduleExceptionRepository scheduleExceptionRepository;

    private final TimeSlotRepository timeSlotRepository;

    private final AgentRepository agentRepository;

    private final CallRepository callRepository;

    private final TeamRepository teamRepository;

    private final LoggerRepository loggerRepository;

    @Autowired
    public AgentController(SecurityService securityService, CallService callService, AgentService agentService, ScheduleExceptionRepository scheduleExceptionRepository, TimeSlotRepository timeSlotRepository, AgentRepository agentRepository, CallRepository callRepository, TeamRepository teamRepository, LoggerRepository loggerRepository) {
        this.securityService = securityService;
        this.callService = callService;
        this.agentService = agentService;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.agentRepository = agentRepository;
        this.callRepository = callRepository;
        this.teamRepository = teamRepository;
        this.loggerRepository = loggerRepository;
    }

    @ModelAttribute
    public Agent loggedInAgent() {
        return this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
    }


    @GetMapping("/mark/{status}")
    public String markStatus(@PathVariable("status") String status) {
        Agent agent = agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        LocalDateTime localDateTime = LocalDateTime.now();
        Logger logger = new Logger();
        logger.setAgent(agent);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        logger.setTimestamp(timestamp);
        if (status.equals("online")) {
            logger.setLogEvent(LogEvent.TURNED_ONLINE);
            agent.setStatus(AgentStatus.ONLINE);
        } else if (status.equals("offline")) {
            logger.setLogEvent(LogEvent.MANUAL_OFFLINE);
            agent.setStatus(AgentStatus.OFFLINE);
        }
        agent.setTimestamp(timestamp);
        this.loggerRepository.save(logger);
        this.agentRepository.save(agent);
        return "main/error";
    }


    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap, Agent currentAgent) {
        modelMap.put("agent", currentAgent);
        CategoryCreationDTO categoryCreationDTO=new CategoryCreationDTO();
        List<Call> calls = this.callService.getAllCalls();
        // parse through calls and keep unsaved ones at the top
        List<Call> unsavedCalls = new ArrayList<>();
        List<Call> saved = new ArrayList<>();
        for (Call call : calls) {
            if (call.isSaved()) {
                saved.add(call);
            } else {
                unsavedCalls.add(call);
            }
            call.setCategory(CallCategory.ADJUSTMENT_DISORDERS);
        }
        Collections.reverse(unsavedCalls);
        Collections.reverse(saved);
        calls.clear();
        calls.addAll(unsavedCalls);
        calls.addAll(saved);
        categoryCreationDTO.setCallList(calls);
        Other other = new Other();
        modelMap.put("role", currentAgent.getRole().toString());
        modelMap.put("other",other);
        modelMap.put("calls",categoryCreationDTO);
        return "main/success";
    }

    @GetMapping("/scheduleException/new")
    public String addNewScheduleException(ModelMap modelMap, Agent currentAgent) {
        SlotForm slot = new SlotForm();
        modelMap.put("slot", slot);
        modelMap.put("role", currentAgent.getRole().toString());
        return "schedule/agent_new_exception";
    }

    @SneakyThrows
    @PostMapping("/scheduleException/new")
    public String acceptException(SlotForm slotForm, Agent loggedInAgent) {
        ScheduleException scheduleException = new ScheduleException();
        scheduleException.setAgent(loggedInAgent);
        TimeSlot timeSlot = this.timeSlotRepository.getById(Long.parseLong(slotForm.getSlotID()));
        scheduleException.setSlot(timeSlot);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateForm = LocalDate.parse(slotForm.getDate(), dateFormatter);
        Date date = Date.valueOf(dateForm);
        scheduleException.setDate(date);
        this.scheduleExceptionRepository.save(scheduleException);
        if (slotForm.getNumberOfRepeats() != null) {
            for (int i = 0; i < slotForm.getNumberOfRepeats(); i++) {
                dateForm = dateForm.plusDays(7);
                date = Date.valueOf(dateForm);
                ScheduleException scheduleException1 = new ScheduleException();
                scheduleException1.setDate(date);
                scheduleException1.setAgent(loggedInAgent);
                scheduleException1.setSlot(timeSlot);
                this.scheduleExceptionRepository.save(scheduleException1);
            }
        }
        return "redirect:/success";
    }


    @PostMapping("/schedule")
    public String getTime(@ModelAttribute("other") Other other) {
                String x=other.getEndtime();
        Agent agent=agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime endtime = LocalTime.parse(x, formatter);
        Time end=Time.valueOf(endtime);
        agent.setTime(end);
        agentRepository.save(agent);
        return "redirect:/success";
    }

    @GetMapping("/team/view")
    public String viewTeam(ModelMap modelMap, Agent loggedInAgent) {
        List<Agent> agents = new ArrayList<>();
        agents.add(loggedInAgent);
        Team team = this.teamRepository.findTeamByAgentsIn(agents);
        if (team == null) {
            return "redirect:/success";
        }
        modelMap.put("team", team);
        modelMap.put("role", loggedInAgent.getRole().toString());
        return "main/viewTeam";
    }

    @PostMapping("/editCall")
    public String addDescription(@ModelAttribute("calls") CategoryCreationDTO categoryCreationDTO) {
        List<Call> calls = categoryCreationDTO.getCallList();
        for (Call call : calls) {
            Call add = callRepository.findById(call.getId()).get();
            add.setDescription(call.getDescription());
            add.setCategory(call.getCategory());
            callRepository.save(add);
        }
        return "redirect:/success";
    }

    @GetMapping("/agent/{agentid}/viewAllCalls")
    public String viewAllAgentCalls(@PathVariable("agentid") Long agentId, ModelMap modelMap) {
        Agent agent = this.agentService.findById(agentId);
        return findPaginatedByAgent(1, "id", "asc", agent, modelMap);
    }

    @GetMapping("/Agentpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,@RequestParam("sortField") String sortField,@RequestParam("sortDir") String sortDir ,Model model){
        int pageSize=10;
        Page<Agent> page= agentService.findPaginated(pageNo,pageSize,sortField,sortDir);
        List<Agent> agentList=page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAgent",agentList);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
        return "agents/viewAgents";
    }

    @GetMapping("/AgentTeam/{pageNo}")
    public String findPaginatedByteam(@PathVariable(value = "pageNo") int pageNo,@RequestParam("sortField") String sortField,@RequestParam("sortDir") String sortDir ,Model model){
        int pageSize=10;
        Agent agent=this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        Team team=this.teamRepository.findByAgentsEquals(agent);
        Page<Agent> page= agentService.findByTeam(team,pageNo,pageSize,sortField,sortDir);
        List<Agent> agentList=page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAgent",agentList);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
        return "agents/viewAgents";
    }

    @GetMapping("/viewAllAgents")
    public String viewAllagents(Model model)
    {
        return findPaginated(1,"name","asc",model);
    }

    @GetMapping("/viewTeam")
    public String viewAgentsinTeam(Model model)
    {
        return findPaginatedByteam(1,"name","asc", model);
    }

    @GetMapping("/pageAgent/{pageNo}")
    public String findPaginatedByAgent(@PathVariable("pageNo") Integer pageNo,
                                       @RequestParam("sortField") String sortField,
                                       @RequestParam("sortDir") String sortDir, Agent agent, ModelMap modelMap) {
        int pageSize = 10;

        Page<Call> page = callService.findAllByAgent(agent, pageNo, pageSize, sortField, sortDir);
        List<Call> listCalls = page.getContent();
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        modelMap.put("listCalls", listCalls);
        return "calls/viewCallsAgent";
    }

    @GetMapping("/team/{teamid}/viewAllCalls")
    public String viewAllTeamCalls(@PathVariable("teamid") Long teamId, ModelMap modelMap) {
        Team team = this.teamRepository.findById(teamId);
        return findPaginatedByTeam(1, "id", "asc", team, modelMap);
    }

    @GetMapping("/teamPage/{pageno}")
    public String findPaginatedByTeam(@PathVariable("pageno") Integer pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir, Team team, ModelMap modelMap) {
        int pageSize = 10;
        Page<Call> page = callService.findAllByTeam(team, pageNo, pageSize, sortField, sortDir);
        List<Call> listCalls = page.getContent();
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelMap.put("listCalls", listCalls);
        return "calls/viewTeamCalls";
    }

}
