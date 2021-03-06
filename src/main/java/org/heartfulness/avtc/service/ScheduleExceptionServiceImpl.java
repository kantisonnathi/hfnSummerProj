package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.ScheduleException;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.repository.ScheduleExceptionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class ScheduleExceptionServiceImpl implements ScheduleExceptionService{

    private final ScheduleExceptionRepository scheduleExceptionRepository;

    public ScheduleExceptionServiceImpl(ScheduleExceptionRepository scheduleExceptionRepository) {
        this.scheduleExceptionRepository = scheduleExceptionRepository;
    }

    @Override
    public Page<ScheduleException> findAllPaginatedByTeamAndDate(Date date, Team team, int pageno, int pagesize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageno-1,pagesize,sort);
        if (team.getAgents() == null) {
            return toPage(new ArrayList<ScheduleException>(), pageable);
        }
        Set<Agent> agents = team.getAgents();
        List<ScheduleException> scheduleExceptions = new ArrayList<>();
        for (Agent agent : agents) {
            Set<ScheduleException> exceptions = agent.getScheduleExceptions();
            for (ScheduleException exception : exceptions) {
                if (exception.getDate().equals(date)) {
                    scheduleExceptions.add(exception);
                }
            }
        }
        return toPage(scheduleExceptions, pageable);
    }

    private Page<ScheduleException> toPage(List<ScheduleException> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Override
    public void save(ScheduleException scheduleException) {
        this.scheduleExceptionRepository.save(scheduleException);
    }

    @Override
    public boolean findEqual(ScheduleException scheduleException) {
        //returns true if there's no other schedule exception with same date, agent and slot
        ScheduleException sc = this.scheduleExceptionRepository.findByDateAndAgentAndSlot(scheduleException.getDate(), scheduleException.getAgent(), scheduleException.getSlot());
        return sc == null;
    }

    @Override
    public ScheduleException findById(Long Id) {
        return this.scheduleExceptionRepository.findById(Id).orElse(null);
    }

    @Override
    public void delete(Long entityId) {
        this.scheduleExceptionRepository.delete(entityId);
    }
}
