package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.enums.AgentRole;
import org.heartfulness.avtc.model.Caller;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.TeamRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    //TODO: rewrite code for adding agent to team
    //TODO: test all team related functionality, figure out what to rewrite
    //TODO: add new department (adding new services and languages)
    //TODO: write code to create teams based on time slot(s) and language
    //TODO: remove code that makes new team based on new team lead
    //TODO: show a list of all the teams that an agent is part of when agent selects view team

    private final SecurityService securityService;
    private final AgentRepository agentRepository;
    private final TeamRepository teamRepository;
    private final AgentService agentService;
    private final CallerService callerService;

    @Autowired
    public AdminController(SecurityService securityService, AgentRepository agentRepository, TeamRepository teamRepository, AgentService agentService, CallerService callerService) {
        this.securityService = securityService;
        this.agentRepository = agentRepository;
        this.teamRepository = teamRepository;
        this.agentService = agentService;
        this.callerService = callerService;
    }

    @ModelAttribute
    public Agent getLoggedInAgent() {
        return this.agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
    }

    @GetMapping("/teams/all")
    public String showAllTeams(ModelMap modelMap, Agent loggedInAgent) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        List<Team> teams = this.teamRepository.findAll();
        modelMap.put("teams", teams);
        return "team/viewTeams";
    }

    @GetMapping("/unassignedAgents")
    public String showAllUnassignedAgents(ModelMap modelMap, Agent loggedInAgent) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        List<Agent> agents = this.agentRepository.findAgentsWithNoTeam();
        modelMap.put("unassignedAgents", agents);
        return "team/unassignedAgents";

    }

    @GetMapping("/newTeam/{agentID}")
    public String makeNewTeam(@PathVariable("agentID") Long agentID, Agent loggedInAgent) {
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        Agent currAgent = this.agentService.findById(agentID);
        Team team = new Team();
        team.setManager(currAgent);
        currAgent.addTeam(team);
        currAgent.setRole(AgentRole.TEAM_LEAD);
        team = this.teamRepository.save(team);
        this.agentService.saveAgent(currAgent);
        return "redirect:/admin/team/" + team.getId();
    }

    //TODO: rewrite this code
    @GetMapping("/team/{id}")
    public String showIndividualTeam(@PathVariable("id") Long teamID, ModelMap modelMap) {

        Team team = this.teamRepository.findById(teamID);

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
        Team currentTeam = this.teamRepository.findById(teamid);
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
        Team currentTeam = this.teamRepository.findById(teamid);
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
        modelMap.put("team", this.teamRepository.findById(teamID));
        modelMap.put("unassignedAgents", unassignedAgents);
        return "team/chooseAgent";
    }

    @GetMapping("/team/{teamid}/add/{agentid}")
    public String addAgentToTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentid") Long agentID, Agent loggedAgent) {
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamRepository.findById(teamid);
        Agent currentAgent = this.agentService.findById(agentID);

        try {
            currentTeam.addAgent(currentAgent);
        } catch (Exception e) {
            return "main/error";
        }

        this.agentService.saveAgent(currentAgent);
        this.teamRepository.save(currentTeam);

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
        return "caller/viewCallers";
    }

    @GetMapping("/agents/all")
    public String viewAllagents(Model model)
    {
        return findPaginated(1,"name","asc",model);
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

}
