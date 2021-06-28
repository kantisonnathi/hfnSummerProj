package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AgentServiceImpl implements AgentService{
    @Autowired
    private AgentRepository agentRepository;

    @Override
    public List<Agent> getAllAgents() {
        return this.agentRepository.findAll();
    }

    @Override
    public void saveAgent(Agent agent) {
       this.agentRepository.save(agent);
    }

    @Override
    public Agent findById(Long id) {
        Optional<Agent> optional = this.agentRepository.findById(id);
        Agent agent = null;
        if (optional.isPresent()) {
            agent = optional.get();
        } else {
            throw new RuntimeException("Agent not found for id :: " + id);
        }
        return agent;
    }

    @Override
    public Agent findBycontactNumber(String phoneNumber) {
        Optional<Agent> optional=this.agentRepository.findByContactNumber(phoneNumber);
       Agent agent=null;
       if(optional.isPresent())
       {
           agent= optional.get();
       }
       else
       {
           throw new RuntimeException("Agent not found for number:: "+phoneNumber);
       }
        return agent;
    }

    @Override
    public List<Agent> findByStatusAndDepartment(Long deptid, int level) {
        return this.agentRepository.getByStatusandDepartment(deptid,level);
    }

    @Override
    public List<Agent> findByTimeNotNull() {
        return this.agentRepository.findAllByTimeNotNull();
    }

    @Override
    public List<Agent> findAgentsByDepartmentsInAndStatusEquals(Set<Department> departments, AgentStatus status) {
        return this.agentRepository.findAgentsByDepartmentsInAndStatusEquals(departments,status);
    }

    @Override
    public List<Agent> findAgentByDepartments(Department departments) {
        return this.agentRepository.findAgentByDepartments(departments);
    }

    @Override
    public Set<Agent> findAgentsByTeamEquals(Team team) {
        return this.agentRepository.findAgentsByTeamEquals(team);
    }

    @Override
    public Page<Agent> findPaginated(int pageno, int pagesize) {
        Pageable pageable= PageRequest.of(pageno-1,pagesize);
        return this.agentRepository.findAll(pageable);
    }

    @Override
    public List<Agent> findAllByLeasedByAndStatus(Call call, AgentStatus status) {
        return this.agentRepository.findAllByLeasedByAndStatus(call,status);
    }

}