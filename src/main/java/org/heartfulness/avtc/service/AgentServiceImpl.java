package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.enums.AgentStatus;
import org.heartfulness.avtc.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.ArrayList;
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
        Optional<Agent> optional = this.agentRepository.findByContactNumber(phoneNumber);
       Agent agent = null;
       if(optional.isPresent()) {
           agent= optional.get();
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
    public List<Agent> findAgentsByTeamEquals(Team team) {
        return this.agentRepository.findAgentsByTeams(team);
    }

    @Override
    public Page<Agent> findPaginated(int pageno, int pagesize,String sortField,String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageno-1,pagesize,sort);
        return this.agentRepository.findAll(pageable);
    }

    @Override
    public Page<Agent> findPaginatedMinusAdmin(int pageno, int pagesize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageno-1,pagesize,sort);
        return this.agentRepository.findAllMinusAdmin(pageable);
    }

    @Override
    public Page<Agent> findByTeam( Team team, int pageno, int pagesize, String sortField, String sortDirection) {
        Sort sort=sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageno-1,pagesize,sort);
        List<Agent> agents= this.agentRepository.findAgentsByTeams(team);
        return toPage(agents, pageable);
    }

    private Page<Agent> toPage(List<Agent> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Override
    public List<Agent> findAllByLeasedByAndStatus(Call call, AgentStatus status) {
        return this.agentRepository.findAllByLeasedByAndStatus(call,status);
    }

    @Override
    public Page<Agent> findAgentsWithNoTeam(int pageno, int pagesize, String sortField, String sortDirection) {
        List<Agent> agents = this.agentRepository.findAgentsWithNoTeam();
        Sort sort=sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageno-1,pagesize,sort);
        return toPage(agents, pageable);
    }

}
