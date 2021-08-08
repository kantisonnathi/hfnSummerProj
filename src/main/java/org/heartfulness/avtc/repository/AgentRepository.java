package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select * from agent where id in (select agents_id from agent_departments where departments_id=?)", nativeQuery = true)
    List<Agent> findAgentByDepartments(Department departments);

    @Query(value = "select * from agent where role != 'ROLE_ADMIN' and id not in (select agents_id from agent_teams)", nativeQuery = true)
    List<Agent> findAgentsWithNoTeam();

    @Query(value = "select * from agent where role != 'ROLE_ADMIN'", nativeQuery = true)
    Page<Agent> findAllMinusAdmin(Pageable pageable);

    @Query(value="select * from Agent a where a.id in (select at.agents_id from agent_teams at where at.teams_id=?)", nativeQuery = true)
    List<Agent> findAgentsByTeams(Team team);

    List<Agent> findAllByLeasedByAndStatus(Call call, AgentStatus status);

    @Query(value = "select * from agent where id in (select agents_id from agent_departments where departments_id=?)", nativeQuery = true)
    List<Agent> findAgentsByDepartmentID(Long depID);

    @NotNull
    List<Agent> findAll();

    @Query(value = "select * from agent where id in (select agents_id from agent_departments where departments_id=? and agents_id not in (select agents_id from agent_teams where teams_id=?))", nativeQuery = true)
    List<Agent> findAllByDepartmentsAndNotInTeam(Department department, Team team);

    Set<Agent> findAgentsByLeasedBy(Call call);

    @Query(value = "select * from agent where id in (select agent_id from calls where calls.id=?)", nativeQuery = true)
    Agent findByCall(Call call);
}
