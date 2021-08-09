package org.heartfulness.avtc.service;


import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface AgentService {
    List<Agent> getAllAgents();

    void saveAgent(Agent agent);

    Agent findById(Long id);

    Agent findBycontactNumber(String phoneNumber);

    List<Agent> findByStatusAndDepartment(Long depid,int level);

    List<Agent> findByTimeNotNull();

    List<Agent> findAgentsByDepartmentsInAndStatusEquals(Set<Department> departments, AgentStatus status);

    List<Agent> findAgentByDepartments(Department departments);

    List<Agent> findAgentsByDepartmentAndStatus(Long departmentID, AgentStatus status);

    List<Agent> findAgentsByTeamEquals(Team team);

    Page<Agent> findPaginated(int pageno, int pagesize,String sortField,String sortDirection);

    Page<Agent> findPaginatedMinusAdmin(int pageNo, int pageSize, String sortField, String sortDirection);

    Page<Agent> findByTeam(Team team,int pageno,int pagesize,String sortField,String sortDirection);

    List<Agent> findAllByLeasedByAndStatus(Call call, AgentStatus status);

    Page<Agent> findAgentsWithNoTeam(int pageNo, int pageSize, String sortField, String sortDir);

    Page<Agent> getAgentsByDepartment(Department department, int pageNo, int pageSize, String sortField, String sortDir);

    Page<Agent> getEligibleAgentsByTeam(Team team, Department department, int pageNo, int pageSize, String sortField, String sortDir);
}
