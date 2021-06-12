package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Department;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;

public interface AgentRepository extends Repository<Agent, Long> {

    Agent findByContactNumber(String contactNumber);

    void save(Agent agent);

    Agent findById(Long id);

    List<Agent> findAll();

    List<Agent> findAllByDepartmentsOrderByTimestamp(Set<Department> list);


}
