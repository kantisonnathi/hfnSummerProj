package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AgentRepository extends Repository<Agent, Long> {

    Agent findByContactNumber(String contactNumber);

    void save(Agent agent);

    Agent findById(Long id);

    List<Agent> findAll();


}
