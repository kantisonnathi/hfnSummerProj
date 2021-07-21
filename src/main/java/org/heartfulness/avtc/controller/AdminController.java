package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.form.SlotsForm;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentRole;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LanguageRepository;
import org.heartfulness.avtc.repository.TeamRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallerService;
import org.heartfulness.avtc.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    //TODO: rewrite code for adding agent to team-Sahithi-Done
    //TODO: add flash messages wherever required - sahithi (trying alerts)-Done
    //TODO: add styling for the flash messages - null
    //TODO: updating agent information - sahithi-
    //TODO: display list of timeslots and language for team - kanti
    //TODO: write code to create teams based on time slot(s) and language - kanti
    //TODO: remove code that makes new team based on new team lead - kanti
    //TODO:Debug agent details-Sahithi

    private final SecurityService securityService;
    private final AgentRepository agentRepository;
    private final TeamRepository teamRepository;
    private final AgentService agentService;
    private final CallerService callerService;
    private final LanguageRepository languageRepository;
    private final TeamService teamService;

    @Autowired
    public AdminController(SecurityService securityService, AgentRepository agentRepository, TeamRepository teamRepository, AgentService agentService, CallerService callerService, LanguageRepository languageRepository, TeamService teamService) {
        this.securityService = securityService;
        this.agentRepository = agentRepository;
        this.teamRepository = teamRepository;
        this.agentService = agentService;
        this.callerService = callerService;
        this.languageRepository = languageRepository;
        this.teamService = teamService;
    }

    private String validation(Agent loggedInAgent, RedirectAttributes redirectAttributes) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            redirectAttributes.addFlashAttribute("message", "You are not authorized to view this page!");
            return "redirect:/success";
        }
        return null;
    }

    @ModelAttribute
    public Agent getLoggedInAgent() {
        return this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
    }

    @GetMapping("/teams/all")
    public String showAllTeams(ModelMap modelMap, Agent loggedInAgent, RedirectAttributes redirectAttributes) {
        if (validation(loggedInAgent, redirectAttributes) == null) {
            return viewAllPaginatedTeams(1, "id", "asc", modelMap);
        }
        return validation(loggedInAgent, redirectAttributes);
    }

    @GetMapping("/team/all/{pageno}")
    public String viewAllPaginatedTeams(@PathVariable("pageno") int pageNo,
                                        @RequestParam("sortField") String sortField,
                                        @RequestParam("sortDir") String sortDir,
                                        ModelMap modelMap) {
        int pageSize = 10;
        Page<Team> page = this.teamService.findAllPaginatedTeams(pageNo,pageSize,sortField,sortDir);

        List<Team> listTeams = page.getContent();

        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalElements());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("role", "ADMIN");
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("list", listTeams);
        modelMap.put("url", "team/all");
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "team/viewTeams";
    }

    @GetMapping("/unassignedAgents")
    public String showAllUnassignedAgents(ModelMap modelMap, Agent loggedInAgent) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        return paginatedUnassigned(1, "id", "asc", modelMap);
    }

    @GetMapping("/unassigned/{pageno}")
    public String paginatedUnassigned(@PathVariable("pageno") int pageNo,
                                     @RequestParam("sortField") String sortField,
                                     @RequestParam("sortDir") String sortDir,
                                     ModelMap modelMap) {
        int pageSize = 10;
        Page<Agent> unassigned = this.agentService.findAgentsWithNoTeam(pageNo,pageSize,sortField,sortDir);
        List<Agent> listTeams = unassigned.getContent();

        modelMap.put("role", "ADMIN");
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", unassigned.getTotalElements());
        modelMap.put("totalItems", unassigned.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("list", unassigned);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "agents/viewUnassignedAgents";
    }

    @PostMapping("/team/new")
    public String saveNewTeam(Agent loggedInAgent, SlotsForm slotsForm, RedirectAttributes redirectAttributes) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        HashSet<TimeSlot> slots = generate(slotsForm);
        Language language = this.languageRepository.findById(Long.parseLong(slotsForm.getLangId())).get();
        Team team = new Team();
        team.setTimeSlots(slots);
        team.setLanguage(language);
        this.teamRepository.save(team);
        //TODO: now, set team lead.
        return "redirect:/admin/team/" + team.getId();
    }

    @GetMapping("/team/new")
    public String makeNewTeam(Agent loggedInAgent, ModelMap modelMap) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        SlotsForm slotsForm = new SlotsForm();
        List<Language> languages = this.languageRepository.findAll();
        modelMap.put("form", slotsForm);
        modelMap.put("languages", languages);
        return "team/newTeam";

    }

    //TODO: add details of slots and languages covered by this team + make agents paginated
    @GetMapping("/team/{id}")
    public String showIndividualTeam(@PathVariable("id") Long teamID, ModelMap modelMap) {

        Team team = this.teamService.findById(teamID);

        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (loggedAgent.getRole().equals(AgentRole.TEAM_LEAD) && loggedAgent.getTeamManaged().getId().equals(team.getId())){
            modelMap.put("team", team);
            return "team/viewSingle";
        }
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        modelMap.put("team", team);
        return "team/viewSingle";
    }

    @GetMapping("/team/{teamid}/makeLead/{agentId}")
    public String makeAdmin(@PathVariable("teamid") Long teamid, @PathVariable("agentId") Long agentID, Agent loggedAgent) {
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamService.findById(teamid);
        Agent newManager = this.agentService.findById(agentID);
        newManager.setRole(AgentRole.TEAM_LEAD);
        Agent oldManager = currentTeam.getManager();
        oldManager.setRole(AgentRole.AGENT);
        currentTeam.setManager(newManager);
        this.teamRepository.save(currentTeam);
        this.agentService.saveAgent(oldManager);
        this.agentService.saveAgent(newManager);
        if (loggedAgent.getRole() == AgentRole.AGENT) {
            return "redirect:/team/view";
        }
        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/team/{teamid}/remove/{agentID}")
    public String removeAgentFromTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentID") Long agentid , Agent loggedAgent) {
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamService.findById(teamid);
        Agent currentAgent = this.agentService.findById(agentid);
        currentTeam.removeAgent(currentAgent);
        this.teamRepository.save(currentTeam);
        this.agentService.saveAgent(currentAgent);

        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/team/{teamid}/addAgent")
    public String addAgentsList(@PathVariable("teamid") Long teamID, ModelMap modelMap, Agent loggedAgent) {
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        List<Agent> unassignedAgents = this.agentRepository.findAgentsWithNoTeam();
        Optional<Team> team1=this.teamRepository.findById(teamID);
       Team team=new Team();
        if(team1.isPresent())
        {
             team=team1.get();
        }
        modelMap.put("team", team);
        modelMap.put("unassignedAgents", unassignedAgents);
        return "team/chooseAgent";
    }

    @GetMapping("/team/{teamid}/add/{agentid}") //TODO: fix this method
    public String addAgentToTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentid") Long agentID, Agent loggedAgent) {
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamService.findById(teamid);
        Agent currentAgent = this.agentService.findById(agentID);

        try {

            currentTeam.addAgent(currentAgent);
            Set<Team> teamSet=currentAgent.getTeam();
            currentAgent.setTeam(teamSet);
        } catch (Exception e) {
            return "main/error";
        }

        this.agentService.saveAgent(currentAgent);
       // this.teamRepository.save(currentTeam);

        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/caller/all")
    public String displayAllCallers(ModelMap modelMap, Agent loggedAgent) {
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN) && !loggedAgent.getRole().equals(AgentRole.TEAM_LEAD)) {
            //not authorized
            return "main/error";
        }
        return findPaginatedCallers(1, "name", "asc", modelMap);
    }

    @GetMapping("/callerpg/{pgno}")
    public String findPaginatedCallers(@PathVariable("pgno") int pageNo,
                   @RequestParam("sortField") String sortField,
                   @RequestParam("sortDir") String sortDir,
                   ModelMap model) {
        int pageSize = 5;

        Page<Caller> page = callerService.findAllPaginatedCallers(pageNo, pageSize, sortField, sortDir);
        List<Caller> listEmployees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", listEmployees);
        //TODO: add top nav to view callers html, use same bootstrap for other html paginated
        return "caller/viewCallers";
    }

    @GetMapping("/agents/all")
    public String viewAllagents(Model model) {
        return findPaginated(1,"name","asc",model);
    }

    @GetMapping("/Agentpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model){
        int pageSize=10;
        Page<Agent> page= agentService.findPaginated(pageNo,pageSize,sortField,sortDir);
        List<Agent> agentList=page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("list",agentList);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
        model.addAttribute("role", AgentRole.ADMIN.toString());
        return "agents/viewAgents";
    }

    @GetMapping("/agent/new")
    public String addNewAgent(ModelMap modelMap) {
        Agent agent = new Agent();
        modelMap.put("agent", agent);
        modelMap.put("role", AgentRole.ADMIN.toString());
        return "agents/newAgentForm";
    }

    @PostMapping(value = "/agent/new")
    public String savingNewAgent(Agent agent) {
        agent.setMissed(0);
        agent.setRole(AgentRole.AGENT);
        this.agentRepository.save(agent);
        return "redirect:/admin/agents/all";
    }

    @GetMapping("/{agentID}/inactive")
    public String markAgentInactive(@PathVariable("agentID") Long agentID) {
        Agent agent = this.agentService.findById(agentID);
        agent.setCertified(false);
        this.agentRepository.save(agent);
        return "redirect:/admin/agents/all";
    }

    @GetMapping("/{agentID}/active")
    public String markAgentActive(@PathVariable("agentID") Long agentID) {
        Agent agent = this.agentService.findById(agentID);
        agent.setCertified(true);
        this.agentRepository.save(agent);
        return "redirect:/admin/agents/all";
    }

    private HashSet<TimeSlot> generate(SlotsForm slotsForm) {
        HashSet<TimeSlot> timeSlots = new HashSet<>(); //ik this is inefficient dont come @ me, ill make it efficient later
        if (slotsForm.isIs1am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(1,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs2am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(2,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs3am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(3,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs4am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(4,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs5am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(5,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs6am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(6,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs7am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(7,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs8am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(8,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs9am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(9, 0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs10am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(10,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs11am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(11,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs12am()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(0,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs1pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(13,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs12pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(12,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs2pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(14,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs3pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(15,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs4pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(16,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs5pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(17,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs6pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(18,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs7pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(19,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs8pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(20,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs9pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(21,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs10pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(22,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs11pm()) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(Time.valueOf(LocalTime.of(23,0)));
            timeSlots.add(slot);
        }
        return timeSlots;
    }

}
