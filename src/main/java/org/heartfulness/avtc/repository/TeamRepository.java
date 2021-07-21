package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findById(Long id);
    Team findTeamByAgentsIn(List<Agent> agents);

    @Query(value = "select * from team where manager_id=?", nativeQuery = true)
    Team findTeamManagedBy(Agent agent);

    Team findByAgentsEquals(Agent agent);

    Page<Team> findAll(Pageable pageable);

    Page<Team> findAllByAgents(Agent agent, Pageable pageable);

}
