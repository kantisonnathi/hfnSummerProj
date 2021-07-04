package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.AgentRole;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SecurityService securityService;

    private final AgentRepository agentRepository;

    private final TeamRepository teamRepository;

    @Autowired
    AgentService agentService;

    @Autowired
    CallerService callerService;


    public AdminController(AgentRepository agentRepository, TeamRepository teamRepository) {
        this.agentRepository = agentRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/lead/team/view")
    public String teamLeadView() {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.TEAM_LEAD)) {
            //not authorized to view this page.
            return "main/error";
        }
        return "main/error";
    }

    @GetMapping("/teams/all")
    public String showAllTeams(ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        List<Team> teams = this.teamRepository.findAll();
        modelMap.put("teams", teams);
        return "team/viewTeams";
    }

    @GetMapping("/unassignedAgents")
    public String showAllUnassignedAgents(ModelMap modelMap) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        Set<Agent> agents = this.agentRepository.findAgentsByTeamEquals(null);
        modelMap.put("unassignedAgents", agents);
        return "team/unassignedAgents";

    }

    @GetMapping("/newTeam/{agentID}")
    public String makeNewTeam(@PathVariable("agentID") Long agentID) {
        Agent loggedInAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        Agent currAgent = this.agentService.findById(agentID);
        Team team = new Team();
        team.setManager(currAgent);
        currAgent.setTeam(team);
        currAgent.setRole(AgentRole.TEAM_LEAD);
        team = this.teamRepository.save(team);
        this.agentRepository.save(currAgent);
        return "redirect:/admin/team/" + team.getId();
    }

    @GetMapping("/team/{id}")
    public String showIndividualTeam(@PathVariable("id") Long teamID, ModelMap modelMap) {

        Team team = this.teamRepository.findById(teamID);

        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (loggedAgent.getRole().equals(AgentRole.TEAM_LEAD) && loggedAgent.getTeam().getId().equals(team.getId())){
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
    public String makeAdmin(@PathVariable("teamid") Long teamid, @PathVariable("agentId") Long agentID) {
        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
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
        this.agentRepository.save(oldManager);
        this.agentRepository.save(newManager);
        if (loggedAgent.getRole() == AgentRole.AGENT) {
            return "redirect:/team/view";
        }
        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/team/{teamid}/remove/{agentID}")
    public String removeAgentFromTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentID") Long agentid) {
        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamRepository.findById(teamid);
        Agent currentAgent = this.agentService.findById(agentid);
        currentTeam.removeAgent(currentAgent);
        this.teamRepository.save(currentTeam);
        this.agentRepository.save(currentAgent);

        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/team/{teamid}/addAgent")
    public String addAgentsList(@PathVariable("teamid") Long teamID, ModelMap modelMap) {
        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Set<Agent> unassignedAgents = this.agentRepository.findAgentsByTeamEquals(null);
        modelMap.put("team", this.teamRepository.findById(teamID));
        modelMap.put("unassignedAgents", unassignedAgents);
        return "team/chooseAgent";
    }

    @GetMapping("/team/{teamid}/add/{agentid}")
    public String addAgentToTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentid") Long agentID) {
        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
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

        this.agentRepository.save(currentAgent);
        this.teamRepository.save(currentTeam);

        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/caller/all")
    public String displayAllCallers(ModelMap modelMap) {
        Agent loggedAgent = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
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
}
