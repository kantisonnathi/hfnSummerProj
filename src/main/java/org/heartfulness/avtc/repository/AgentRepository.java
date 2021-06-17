package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.AgentStatus;
import org.heartfulness.avtc.model.Department;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AgentRepository extends Repository<Agent, Long> {

    Agent findByContactNumber(String contactNumber);

    void save(Agent agent);

    Agent findById(Long id);

    List<Agent> findAll();

    /*@Query("select agent from Agent agent join AgentDepartment join Department department where department.id=:id")
    List<Agent> findAgentsByDepartmentsDepartment(Long id);*/

    List<Agent> findAgentsByDepartmentsInAndStatusEquals(Set<Department> departments, AgentStatus status);

    List<Agent> findAgentsByTeamEquals(Team team);

}
