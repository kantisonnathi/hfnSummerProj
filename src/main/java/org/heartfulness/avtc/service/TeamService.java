package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeamService {

    Page<Team> findAllPaginatedTeams(int pageno, int pagesize, String sortField, String sortDir);

    Team findById(Long id);

    List<Team> findTeamsByAgent(Agent agent);

    Team findByAgentsEquals(Agent agent);

    Page<Team> findAllTeamsUnderAgent(Agent agent, int pageNo, int pageSize, String sortField, String sortDir);

}
