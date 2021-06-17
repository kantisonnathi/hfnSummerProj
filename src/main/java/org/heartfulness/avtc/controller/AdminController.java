package org.heartfulness.avtc.controller;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.AgentRole;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.TeamRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SecurityService securityService;

    private final AgentRepository agentRepository;

    private final TeamRepository teamRepository;

    public AdminController(AgentRepository agentRepository, TeamRepository teamRepository) {
        this.agentRepository = agentRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/teams/all")
    public String showAllTeams(ModelMap modelMap) {
        Agent loggedInAgent = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedInAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized to view this page.
            return "main/error";
        }
        List<Team> teams = this.teamRepository.findAll();
        modelMap.put("teams", teams);
        return "team/viewTeams";
    }
    
    @GetMapping("/team/{id}")
    public String showIndividualTeam(@PathVariable("id") Long teamID, ModelMap modelMap) {

        Team team = this.teamRepository.findById(teamID);

        Agent loggedAgent = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
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

    @GetMapping("/team/{teamid}/remove/{agentID}")
    public String removeAgentFromTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentID") Long agentid) {
        Agent loggedAgent = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamRepository.findById(teamid);
        Agent currentAgent = this.agentRepository.findById(agentid);
        currentTeam.removeAgent(currentAgent);
        this.teamRepository.save(currentTeam);
        this.agentRepository.save(currentAgent);

        return "redirect:/admin/team/" + teamid;
    }

    @GetMapping("/team/{teamid}/addAgent")
    public String addAgentsList(@PathVariable("teamid") Long teamID, ModelMap modelMap) {
        Agent loggedAgent = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        List<Agent> unassignedAgents = this.agentRepository.findAgentsByTeamEquals(null);
        modelMap.put("team", this.teamRepository.findById(teamID));
        modelMap.put("unassignedAgents", unassignedAgents);
        return "team/chooseAgent";
    }

    @GetMapping("/team/{teamid}/add/{agentid}")
    public String addAgentToTeam(@PathVariable("teamid") Long teamid, @PathVariable("agentid") Long agentID) {
        Agent loggedAgent = this.agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
        if (!loggedAgent.getRole().equals(AgentRole.ADMIN)) {
            //not authorized
            return "main/error";
        }
        Team currentTeam = this.teamRepository.findById(teamid);
        Agent currentAgent = this.agentRepository.findById(agentID);

        try {
            currentTeam.addAgent(currentAgent);
        } catch (Exception e) {
            return "main/error";
        }

        this.agentRepository.save(currentAgent);
        this.teamRepository.save(currentTeam);

        return "redirect:/admin/team/" + teamid;
    }
}
