package org.heartfulness.avtc.service;


import org.heartfulness.avtc.model.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
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
    Set<Agent> findAgentsByTeamEquals(Team team);
    Page<Agent> findPaginated(int pageno, int pagesize);
    List<Agent> findAllByLeasedByAndStatus(Call call, AgentStatus status);
}
