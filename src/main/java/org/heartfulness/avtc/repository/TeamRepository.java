package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TeamRepository extends Repository<Team, Long> {

    List<Team> findAll();

    Team findById(Long id);

    Team save(Team team);

    Team findTeamByAgentsIn(List<Agent> agents);

}
