package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.AgentStatus;
import org.heartfulness.avtc.model.Department;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

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
    List<Agent> findAgentByDepartments(Department departments);
    Set<Agent> findAgentsByTeamEquals(Team team);
   @Query(value="select * from Agent a where a.id in(select d.agents_id from agent_departments d where d.departments_id=?) and a.status=0",nativeQuery = true)
    List<Agent> getByStatusandDepartment(Long deptid);

}
