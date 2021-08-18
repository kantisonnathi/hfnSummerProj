package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.form.AgentForm;
import org.heartfulness.avtc.form.SlotsForm;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentRole;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.security.auth.models.User;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallService;
import org.heartfulness.avtc.service.CallerService;
import org.heartfulness.avtc.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Transactional
@RequestMapping("/admin")
public class AdminController {

    //TODO: updating agent information - sahithi-
    //TODO:Debug agent details-Sahithi

    private final AgentRepository agentRepository;
    private final TeamRepository teamRepository;
    private final AgentService agentService;
    private final CallerService callerService;
    private final LanguageRepository languageRepository;
    private final TeamService teamService;
    private final TimeSlotRepository timeSlotRepository;
    private final ServiceRepository serviceRepository;
    private final CallService callService;
    private final SecurityService securityService;
   private final DepartmentRepository departmentRepository;
    @Autowired
    public AdminController(AgentRepository agentRepository, TeamRepository teamRepository, AgentService agentService, CallerService callerService, LanguageRepository languageRepository, TeamService teamService, TimeSlotRepository timeSlotRepository, ServiceRepository serviceRepository, CallService callService, SecurityService securityService, DepartmentRepository departmentRepository) {
        this.agentRepository = agentRepository;
        this.teamRepository = teamRepository;
        this.agentService = agentService;
        this.callerService = callerService;
        this.languageRepository = languageRepository;
        this.teamService = teamService;
        this.timeSlotRepository = timeSlotRepository;
        this.serviceRepository = serviceRepository;
        this.callService = callService;
        this.securityService = securityService;
        this.departmentRepository = departmentRepository;
    }

    @ModelAttribute
    public Agent getLoggedInAgent(@AuthenticationPrincipal User user, ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(user.getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        return loggedInAgent;
    }

    private String validation(@ModelAttribute Agent loggedInAgent, RedirectAttributes redirectAttributes) {
        if (loggedInAgent == null) {
            return "redirect:/main";
        }
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
            //not authorized to view this page.
            redirectAttributes.addFlashAttribute("message", "You are not authorized to view this page!");
            return "redirect:/success";
        }
        return null;
    }

    @GetMapping("/teams/all") //displays a list of teams
    public String showAllTeams(ModelMap modelMap, RedirectAttributes redirectAttributes) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
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
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        int pageSize = 10;
        Page<Team> page = this.teamService.findAllPaginatedTeams(pageNo,pageSize,sortField,sortDir);

        List<Team> listTeams = page.getContent();

        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());
        modelMap.put("role", "ROLE_ADMIN");
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("list", listTeams);
        modelMap.put("url", "/team/all/");
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "team/viewTeamsPaginated";
    }

    @GetMapping("/team/{teamId}/add")
    public String addAgentsToTeam(@PathVariable("teamId") Long teamId, ModelMap modelMap) {
        return paginatedAddAgentsToTeam(1, "id", "asc", teamId, modelMap);
    }

    @GetMapping("/team/{teamId}/add/{pgNo}")
    public String paginatedAddAgentsToTeam(@PathVariable("pgNo") int pageNo,
                                           @RequestParam("sortField") String sortField,
                                           @RequestParam("sortDir") String sortDir,
                                           @PathVariable("teamId") Long teamId,
                                           ModelMap modelMap) {
        int pageSize = 10;
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());

        Team currentTeam = this.teamService.findById(teamId);
        Department department = this.departmentRepository.findByServiceAndLanguage(currentTeam.getService(), currentTeam.getLanguage());
        Page<Agent> agents = this.agentService.getEligibleAgentsByTeam(currentTeam, department, pageNo, pageSize, sortField, sortDir);
        String url = "/team/" + teamId + "/add/";
        modelMap.put("url", url);
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", agents.getTotalPages());
        modelMap.put("totalItems", agents.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("team", currentTeam);
        modelMap.put("list", agents.getContent());
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "agents/viewAgentsPaginated";
    }

    @GetMapping("/team/{teamId}/add/agent/{agentId}")
    public String addSpecificAgentToTeam(@PathVariable("teamId") Long teamId, @PathVariable("agentId") Long agentId) {
        Agent agentToAdd = this.agentService.findById(agentId);
        Team team = this.teamService.findById(teamId);
        team.addAgent(agentToAdd);
        this.teamRepository.save(team);
        return "redirect:/admin/team/" + team.getId();
    }

    /*@GetMapping("/unassignedAgents")
    public String showAllUnassignedAgents(ModelMap modelMap) {
        *//*if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }*//*
        return paginatedUnassigned(1, "id", "asc", modelMap);
    }

    @GetMapping("/unassigned/{pageno}")
    public String paginatedUnassigned(@PathVariable("pageno") int pageNo,
                                     @RequestParam("sortField") String sortField,
                                     @RequestParam("sortDir") String sortDir,
                                     ModelMap modelMap) {
        int pageSize = 10;
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        Page<Agent> unassigned = this.agentService.findAgentsWithNoTeam(pageNo,pageSize,sortField,sortDir);
        List<Agent> listTeams = unassigned.getContent();

        modelMap.put("role", "ADMIN");
        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", unassigned.getTotalElements());
        modelMap.put("totalItems", unassigned.getTotalElements());
        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("list", listTeams);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "agents/viewUnassignedAgents";
    }*/

    @PostMapping("/team/new")
    public String saveNewTeam(SlotsForm slotsForm, RedirectAttributes redirectAttributes) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        HashSet<TimeSlot> slots = generate(slotsForm);
        Language language = this.languageRepository.findById(Long.parseLong(slotsForm.getLangId())).get();
        Team team = new Team();
        team.setLanguage(language);
        Service service = this.serviceRepository.findById(Long.parseLong(slotsForm.getServId())).get();
        team.setService(service);
        team.setDescription(slotsForm.getDescription());
        team.setManager(null);
        team.setTimeSlots(slots);
        this.teamRepository.save(team);
        //this.timeSlotRepository.saveAll(slots);
        return "redirect:/admin/team/" + team.getId();
    }

    @GetMapping("/team/new")
    public String makeNewTeam(ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        SlotsForm slotsForm = new SlotsForm();
        List<Language> languages = this.languageRepository.findAll();
        List<Service> services = this.serviceRepository.findAll();
        modelMap.put("form", slotsForm);
        modelMap.put("languages", languages);
        modelMap.put("services", services);
        return "team/newTeam";

    }

    @GetMapping("/team/{id}")
    public String showIndividualTeam(@PathVariable("id") Long teamID,ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        Team team = this.teamService.findById(teamID);
        /*if (loggedAgent.getRole().equals(AgentRole.TEAM_LEAD) && loggedAgent.getTeamManaged().getId().equals(team.getId())){
            modelMap.put("team", team);
            return "team/viewSingle";
        }*/
        /*if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }*/
        Set<TimeSlot> slots = team.getTimeSlots();
        int min = 0;
        int max = 0;
        for (TimeSlot t : slots) {
            if (min == 0) {
                min = t.getStartTime().toLocalTime().getHour();
                max = t.getEndTime().toLocalTime().getHour();
            } else {
                int current = t.getStartTime().toLocalTime().getHour();
                if (current < min) {
                    min = current;
                }
                current = t.getEndTime().toLocalTime().getHour();
                if (current > max) {
                    max = current;
                }
            }
        }
        modelMap.put("interval", min + " - " + max);
        modelMap.put("team", team);
        return "team/viewSingle";
    }

    @GetMapping("/team/{teamid}/makeLead/{agentId}")
    public String makeAdmin(@PathVariable("teamid") Long teamid, @PathVariable("agentId") Long agentID) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamService.findById(teamid);
        Agent newManager = this.agentService.findById(agentID);
        newManager.setRole(AgentRole.ROLE_TEAM_LEAD);
        Agent oldManager = currentTeam.getManager();
        if (oldManager != null) {
            oldManager.setRole(AgentRole.ROLE_AGENT);
            this.agentService.saveAgent(oldManager);
        }
        currentTeam.setManager(newManager);
        this.teamRepository.save(currentTeam);
        this.agentService.saveAgent(newManager);
        if (loggedInAgent.getRole() == AgentRole.ROLE_AGENT) {
            return "redirect:/team/view";
        }
        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/team/{teamid}/remove/{agentID}")
    public String removeAgentFromTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentID") Long agentid) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
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

    /*@GetMapping("/team/{teamid}/addAgent")
    public String addAgentsList(@PathVariable("teamid") Long teamID, ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
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
    }*/

    /*@GetMapping("/team/{teamid}/add/{agentid}")
    public String addAgentToTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentid") Long agentID) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN)) {
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
    }*/

    @GetMapping("/caller/all")
    public String displayAllCallers(ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("role", loggedInAgent.getRole().toString());
        if (!loggedInAgent.getRole().equals(AgentRole.ROLE_ADMIN) && !loggedInAgent.getRole().equals(AgentRole.ROLE_TEAM_LEAD)) {
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
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        model.put("role", loggedInAgent.getRole().toString());
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
        Page<Agent> page= agentService.findPaginatedAgents(pageNo,pageSize,sortField,sortDir);
        List<Agent> agentList=page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("list",agentList);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
        model.addAttribute("role", AgentRole.ROLE_ADMIN.toString());
        return "agents/viewAgents";
    }

    @GetMapping("/agent/new")
    public String addNewAgent(ModelMap modelMap) {
        AgentForm agent = new AgentForm();
        modelMap.put("agent", agent);
        modelMap.put("role", AgentRole.ROLE_ADMIN.toString());
        return "agents/newAgentForm";
    }

    @PostMapping(value = "/agent/new")
    public String savingNewAgent(AgentForm agentForm) {
        Agent agent = agentForm.getAgent();
        agent.setMissed(0);
        agent.setCertified(true);
        if (agentForm.getDob() != null && !agentForm.getDob().equals("")) {
            agent.setDOB(agentForm.convertedDOB());
        }
        if (agentForm.getCertifiedDate() != null && !agentForm.getCertifiedDate().equals("")) {
            agent.setCertifiedDate(agentForm.convertedCertifiedDate());
        }
        if (agentForm.getTrainingDate() != null && !agentForm.getTrainingDate().equals("")) {
            agent.setLastTrainingDate(agentForm.convertedTrainingDate());
        }
        agent.setRole(AgentRole.ROLE_AGENT);
        this.agentRepository.save(agent);
        return "redirect:/admin/agents/all";
    }

    @GetMapping("/{agentID}/{status}")
    public String markAgentInactive(@PathVariable("agentID") Long agentID, @PathVariable("status") String status) {
        Agent agent = this.agentService.findById(agentID);
        if (status.equals("inactive")) {
            agent.setActive(false);
        } else if (status.equals("active")) {
            agent.setActive(true);
        }
        this.agentRepository.save(agent);
        return "redirect:/admin/agents/all";
    }

    private HashSet<TimeSlot> generate(SlotsForm slotsForm) {
        HashSet<TimeSlot> timeSlots = new HashSet<>(); //ik this is inefficient dont come @ me, ill make it efficient later
        if (slotsForm.isIs1am()) {
            Time time = Time.valueOf(LocalTime.of(1,0));
            TimeSlot slot = this.timeSlotRepository.findByStartTime(time);
            timeSlots.add(slot);
        }
        if (slotsForm.isIs2am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(2,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs3am()) {
            TimeSlot slot  = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(3,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs4am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(4,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs5am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(5,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs6am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(6,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs7am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(7,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs8am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(8,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs9am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(9, 0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs10am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(10,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs11am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(11,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs12am()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(0,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs1pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(13,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs12pm()) {
            TimeSlot slot = timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(12,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs2pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(14,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs3pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(15,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs4pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(16,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs5pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(17,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs6pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(18,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs7pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(19,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs8pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(20,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs9pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(21,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs10pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(22,0)));
            timeSlots.add(slot);
        }
        if (slotsForm.isIs11pm()) {
            TimeSlot slot = this.timeSlotRepository.findByStartTime(Time.valueOf(LocalTime.of(23,0)));
            timeSlots.add(slot);
        }
        return timeSlots;
    }


    @GetMapping("/viewDepartments")
    public String showDepts(ModelMap modelMap)
    {
        List<Department> departments = departmentRepository.findAll();
        modelMap.put("departments",departments);
        return "agents/viewDepartments";
    }

    @GetMapping("/team/{teamId}/{status}")
    public String markTeamStatus(@PathVariable("teamId") Long teamId, @PathVariable("status") String status) {
        Team team = this.teamService.findById(teamId);
        if (status.equals("inactive")) {
            team.setActive(false);
        } else if (status.equals("active")) {
            team.setActive(true);
        }
        this.teamRepository.save(team);
        return "redirect:/admin/team/" + team.getId();
    }
}
