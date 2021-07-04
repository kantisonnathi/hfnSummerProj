package org.heartfulness.avtc.repository;

import com.google.api.gax.paging.Page;
import org.heartfulness.avtc.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AgentRepository extends JpaRepository<Agent,Long> {
    Optional<Agent> findByContactNumber(String contactNumber);
    @Query(value="select * from Agent a where a.id in(select d.agents_id from agent_departments d where d.departments_id=?) and a.status=0 and a.level=?",nativeQuery = true)
    List<Agent> getByStatusandDepartment(Long deptid, int level);
    @Query(value = "select * from Agent a  where a.end_time is not null",nativeQuery = true)
    List<Agent> findAllByTimeNotNull();
    List<Agent> findAgentsByDepartmentsInAndStatusEquals(Set<Department> departments, AgentStatus status);
    List<Agent> findAgentByDepartments(Department departments);
    List<Agent> findAgentsByTeamEquals(Team team);
    List<Agent> findAllByLeasedByAndStatus(Call call, AgentStatus status);
    @NotNull
    List<Agent> findAll();

}
