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

}
