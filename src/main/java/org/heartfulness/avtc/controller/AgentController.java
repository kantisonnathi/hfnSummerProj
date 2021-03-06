package org.heartfulness.avtc.controller;

import lombok.SneakyThrows;
import org.heartfulness.avtc.form.SlotForm;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.AfterCallClasses.CategoryCreationDTO;
import org.heartfulness.avtc.model.enums.AgentRole;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.heartfulness.avtc.model.enums.CallCategory;
import org.heartfulness.avtc.model.enums.LogEvent;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Transactional
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgentController {

    //TODO: remove all the redundant html pages, add URL attr instead

    private final SecurityService securityService;
    private final CallService callService;
    private final AgentService agentService;
    private final ScheduleExceptionService scheduleExceptionService;
    private final TimeSlotRepository timeSlotRepository;
    private final AgentRepository agentRepository;
    private final CallRepository callRepository;
    private final TeamService teamService;
    private final CallerService callerService;
    private final LoggerRepository loggerRepository;

    @Autowired
    public AgentController(SecurityService securityService, CallService callService, AgentService agentService, ScheduleExceptionService scheduleExceptionService, TimeSlotRepository timeSlotRepository, AgentRepository agentRepository, CallRepository callRepository, TeamService teamService, CallerService callerService, LoggerRepository loggerRepository) {
        this.securityService = securityService;
        this.callService = callService;
        this.agentService = agentService;
        this.scheduleExceptionService = scheduleExceptionService;
        this.timeSlotRepository = timeSlotRepository;
        this.agentRepository = agentRepository;
        this.callRepository = callRepository;
        this.teamService = teamService;
        this.callerService = callerService;
        this.loggerRepository = loggerRepository;
    }

    @ModelAttribute
    public Agent loggedInAgent(ModelMap modelMap) {
        Agent agent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", agent.getRole().toString());
        return agent;
    }

    @ModelAttribute
    private String validation(Agent loggedInAgent) {
        if (loggedInAgent == null) {
            return "redirect:/main";
        }
        return null;
    }


    @GetMapping("/mark/{s}")
    public String markStatus(@PathVariable("s") String s) {
        Agent agent = agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        LocalDateTime localDateTime = LocalDateTime.now();
        Logger logger = new Logger();
        logger.setAgent(agent);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        logger.setTimestamp(timestamp);
        if (s.equals("online")) {
            logger.setLogEvent(LogEvent.TURNED_ONLINE);
            agent.setStatus(AgentStatus.ONLINE);
        } else if (s.equals("offline")) {
            logger.setLogEvent(LogEvent.MANUAL_OFFLINE);
            agent.setStatus(AgentStatus.OFFLINE);
        }
        agent.setTimestamp(timestamp);
        this.loggerRepository.save(logger);
        this.agentRepository.save(agent);
        return "main/error";
    }


    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap) {
        Agent agent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", agent.getRole().toString());
        modelMap.put("agent", agent);
        System.out.println(agent.getId() + "\n\n");
        /*CategoryCreationDTO categoryCreationDTO=new CategoryCreationDTO();
        categoryCreationDTO.setCallList(sortingCalls());*/

        Other other = new Other();
        //modelMap.put("role", currentAgent.getRole().toString());
        modelMap.put("other",other);
        //modelMap.put("calls",categoryCreationDTO);

        return "main/success";
    }

    @GetMapping("/scheduleException/new")
    public String addNewScheduleException(ModelMap modelMap) {
        Agent agent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", agent.getRole().toString());
        SlotForm slot = new SlotForm();
        modelMap.put("slot", slot);
        return "schedule/agent_new_exception";
    }

    @SneakyThrows
    @PostMapping("/scheduleException/new")
    public String acceptException(SlotForm slotForm) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        ScheduleException scheduleException = new ScheduleException();
        scheduleException.setAgent(loggedInAgent);
        TimeSlot timeSlot = this.timeSlotRepository.getById(Long.parseLong(slotForm.getSlotID()));
        scheduleException.setSlot(timeSlot);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateForm = LocalDate.parse(slotForm.getDate(), dateFormatter);
        Date date = Date.valueOf(dateForm);
        scheduleException.setDate(date);
        if (this.scheduleExceptionService.findEqual(scheduleException)) {
            scheduleException.setAccepted(false);
            this.scheduleExceptionService.save(scheduleException);
        }
        if (slotForm.getNumberOfRepeats() != null) {
            for (int i = 0; i < slotForm.getNumberOfRepeats(); i++) {
                dateForm = dateForm.plusDays(7);
                date = Date.valueOf(dateForm);
                ScheduleException scheduleException1 = new ScheduleException();
                scheduleException1.setDate(date);
                scheduleException1.setAgent(loggedInAgent);
                scheduleException1.setSlot(timeSlot);
                scheduleException1.setAccepted(false);
                if (this.scheduleExceptionService.findEqual(scheduleException1)) {
                    this.scheduleExceptionService.save(scheduleException1);
                }
            }
        }
        return "redirect:/success";
    }

    @GetMapping("/call/{callId}/edit")
    public String editCall(@PathVariable("callId") Long callId, ModelMap modelMap) {
        Call call = this.callService.findById(callId);
        Caller caller = call.getCaller();
        modelMap.put("caller", caller);
        modelMap.put("call", call);
        return "main/callDetails";
    }

    @PostMapping("/call/save")
    public String saveCall(Call call, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Call details have been saved");
        call.setCaller(this.callerService.findByID(call.getCaller().getId()));
        this.callService.saveCall(call);
        return "redirect:/call/" + call.getId() + "/edit";
    }

    @PostMapping("/{callId}/caller/save")
    public String saveCaller(Caller caller, RedirectAttributes redirectAttributes,
                             @PathVariable("callId") Long callId) {
        redirectAttributes.addFlashAttribute("message", "Caller details have been saved");
        this.callerService.save(caller);
        return "redirect:/call/" + callId + "/edit";
    }

    @PostMapping("/schedule")
    public String getTime(@ModelAttribute("other") Other other) {
                String x=other.getEndtime();
        Agent agent=agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime endtime = LocalTime.parse(x, formatter);
        Time end=Time.valueOf(endtime);
        agent.setEndTime(end);
        agentRepository.save(agent);
        return "redirect:/success";
    }

    @GetMapping("/team/view")
    public String viewTeam(ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        List<Agent> agents = new ArrayList<>();
        agents.add(loggedInAgent);
        List<Team> teams = this.teamService.findTeamsByAgent(loggedInAgent);
        if (teams == null) {
            return "redirect:/success";
        }
        modelMap.put("list", teams);
        return "team/viewTeams";
    }

    @PostMapping("/editCall")
    public String addDescription(@ModelAttribute("calls") CategoryCreationDTO categoryCreationDTO) {
        List<Call> calls = categoryCreationDTO.getCallList();
        for (Call call : calls) {
            Call add = callService.findById(call.getId());
            add.setDescription(call.getDescription());
            add.setCategory(call.getCategory());
            add.setEscalation(call.getEscalation());
            add.setReviewFlag(call.getReviewFlag());
            callRepository.save(add);
        }
        return "redirect:/success";
    }

    @GetMapping("/agent/{agentID}/calls")
    public String viewAllCallsUnderAgent(@PathVariable("agentID") long agentID, ModelMap modelMap,
                                         RedirectAttributes redirectAttributes) {
        Agent agent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", agent.getRole().toString());
        if (agent.getId().equals(agentID) || agent.getRole().equals(AgentRole.ROLE_ADMIN)) {
            return paginatedCallsUnderAgent(1,"id", "asc", agentID, modelMap, redirectAttributes);
        }
        redirectAttributes.addFlashAttribute("message", "You are not authorized to view this page!");
        return "redirect:/success";
    }

    @GetMapping("/agent/{agentID}/calls/{pgno}")
    public String paginatedCallsUnderAgent(@PathVariable("pgno") Integer pageNo,
                                           @RequestParam("sortField") String sortField,
                                           @RequestParam("sortDir") String sortDir,
                                           @PathVariable("agentID") Long agentID, ModelMap modelMap,
                                           RedirectAttributes redirectAttributes) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        if (loggedInAgent.getId().equals(agentID) || loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
            Agent agent = this.agentService.findById(agentID);
            int pageSize = 10;
            Page<Call> calls = this.callService.findAllByAgent(agent, pageNo, pageSize,sortField, sortDir);
            List<Call> listCalls = sortingCalls(calls.getContent());
            modelMap.put("url", "/agent/" + agent.getId() + "/calls/" + pageNo);
            modelMap.put("currentPage", pageNo);
            modelMap.put("totalPages", calls.getTotalPages());
            modelMap.put("totalItems", calls.getTotalElements());
            modelMap.put("sortField", sortField);
            modelMap.put("sortDir", sortDir);
            modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
            modelMap.put("listCalls", listCalls);
            return "calls/viewCalls";
        }
        redirectAttributes.addFlashAttribute("message", "You are not authorized to view this page!");
        return "redirect:/success";


    }

    /*@GetMapping("/agent/{agentid}/viewAllCalls")
    public String viewAllAgentCalls(@PathVariable("agentid") Long agentId, ModelMap modelMap) {
        Agent agent = this.agentService.findById(agentId);
        return findPaginatedByAgent(1, "id", "asc", agent, modelMap);
    }*/


    @GetMapping("/team/agents") //viewing agents in a team
    public String viewAgentsinTeam(ModelMap model) {
        return findPaginatedByteam(1,"name","asc", model);
    }

    @GetMapping("/AgentTeam/{pageNo}")
    public String findPaginatedByteam(@PathVariable(value = "pageNo") int pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir ,ModelMap model){
        int pageSize=10;
        Agent agent=this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        Team team=this.teamService.findByAgentsEquals(agent);
        Page<Agent> page= agentService.findByTeam(team,pageNo,pageSize,sortField,sortDir);
        paginatedModelMapPopulation(model, page, pageNo, pageSize, sortDir, sortField);
        return "agents/viewAgents";
    }

    /*@GetMapping("/pageAgent/{pageNo}")
    public String findPaginatedByAgent(@PathVariable("pageNo") Integer pageNo,
                                       @RequestParam("sortField") String sortField,
                                       @RequestParam("sortDir") String sortDir, Agent agent, ModelMap modelMap) {
        int pageSize = 10;
        Page<Call> page = callService.findAllByAgent(agent, pageNo, pageSize, sortField, sortDir);
        paginatedModelMapPopulation(modelMap, page, pageNo, pageSize, sortDir, sortField);
        return "calls/viewCallsAgent";
    }*/

    private <T> void paginatedModelMapPopulation(ModelMap modelMap, Page<T> page, int pageNo, int pageSize,
                                                 String sortDir, String sortField) {
        List<T> list = page.getContent();
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelMap.put("list", list);
    }

    @GetMapping("/team/{teamid}/viewAllCalls")
    public String viewAllTeamCalls(@PathVariable("teamid") Long teamId, ModelMap modelMap) {
        Team team = this.teamService.findById(teamId);
        return findPaginatedByTeam(1, "id", "asc", team, modelMap);
    }

    @GetMapping("/teamPage/{pageno}")
    public String findPaginatedByTeam(@PathVariable("pageno") Integer pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir, Team team, ModelMap modelMap) {
        int pageSize = 10;
        Page<Call> page = callService.findAllByTeam(team, pageNo, pageSize, sortField, sortDir);
        paginatedModelMapPopulation(modelMap, page, pageNo, pageSize, sortDir, sortField);
        return "calls/viewTeamCalls";
    }

    @GetMapping("/view/team")
    public String viewAllTeamsForAgent(ModelMap modelMap) {
        Agent agent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", agent.getRole().toString());
        return paginated(1, "id", "asc", modelMap);
    }

    @GetMapping("/team/{pageNo}")
    public String paginated(@PathVariable("pageNo") Integer pageNo,
                            @RequestParam("sortField") String sortField,
                            @RequestParam("sortDir") String sortDir, ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        int pageSize = 10;
        Page<Team> page = this.teamService.findAllTeamsUnderAgent(loggedInAgent, pageNo, pageSize, sortField, sortDir);
        paginatedModelMapPopulation(modelMap, page, pageNo, pageSize, sortDir, sortField);
        modelMap.put("url", "team");
        return "team/viewTeamsPaginated";
    }

    @GetMapping("/view/schedule")
    public String viewSelfSchedule(ModelMap modelMap) {
        //max 24, don't need to paginate
        Agent agent = this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        modelMap.put("role", agent.getRole().toString());
        Set<ScheduleException> list = agent.getScheduleExceptions();
        list.removeIf(s -> !s.getAccepted() || !s.getDate().equals(Date.valueOf(LocalDate.now())));
        modelMap.put("schedules", list);
        return "schedule/viewSelf";
    }

    @GetMapping("/agent/{agentId}")
    public String showAgentDetails(@PathVariable("agentId") Long agentID, ModelMap modelMap) {
        Agent agent = this.agentService.findById(agentID);
        modelMap.put("agent", agent);
        modelMap.put("role", agent.getRole().toString());
        return "agents/viewAgent";
    }

    private List<Call> sortingCalls(List<Call> calls) {
        /*
        * this method sorts calls so that the the most recent calls are unsaved
        * */

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
        List<Call> answer=new ArrayList<>();
        answer.addAll(unsavedCalls);
        answer.addAll(saved);
        return answer;
    }

}
