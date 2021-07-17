package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Page<Team> findAllPaginatedTeams(int pageno, int pagesize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageno - 1, pagesize, sort);
        return this.teamRepository.findAll(pageable);
    }

    @Override
    public Team findById(Long id) {
        return this.teamRepository.findById(id).orElse(null);
    }

    @Override
    public Team findTeamByAgentsIn(List<Agent> agents) {
        return this.teamRepository.findTeamByAgentsIn(agents);
    }

    @Override
    public Team findByAgentsEquals(Agent agent) {
        return this.teamRepository.findByAgentsEquals(agent);
    }

    @Override
    public Page<Team> findAllTeamsUnderAgent(Agent agent, int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.teamRepository.findAllByAgents(agent, pageable);
    }
}
