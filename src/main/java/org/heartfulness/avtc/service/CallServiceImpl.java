package org.heartfulness.avtc.service;

import org.heartfulness.avtc.controller.AgentController;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Call;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class CallServiceImpl implements CallService {

    @Autowired
    private CallRepository callRepository;

    @Autowired
    private AgentService agentService;

    @Override
    public List<Call> getAllCalls() {
        return this.callRepository.findAll();
    }

    @Override
    public Call findById(Long id) {
        Optional<Call> optional = this.callRepository.findById(id);
        Call call = null;
        if (optional.isPresent()) {
            call = optional.get();
        } else {
            throw new RuntimeException("call not found for id :: " + id);
        }
        return call;
    }

    @Override
    public void saveCall(Call call) {
        this.callRepository.save(call);
    }

    @Override
    public Page<Call> findAllByAgent(Agent agent, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callRepository.findAllPaginatedByAgent(agent, pageable);
    }

    @Override
    public Page<Call> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callRepository.findAll(pageable);
    }

    @Override
    public Call findByUid(String uid) {
        Optional<Call> call = this.callRepository.findByUid(uid);
        return call.orElse(null);
    }

    @Override
    public Page<Call> findAllByTeam(Team team, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        List<Call> calls = new ArrayList<>();
        List<Agent> agents = this.agentService.findAgentsByTeamEquals(team);
        for (Agent agent : agents) {
            calls.addAll(this.callRepository.findAllByAgent(agent));
        }
        return toPage(calls, pageable);
    }


    private Page<Call> toPage(List<Call> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<Call>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<Call>(list.subList(start, end), pageable, list.size());
    }

    @Override
    public Integer countCallsInADay(Date date) {
        return this.callRepository.countAllByDate(date);
    }

}
