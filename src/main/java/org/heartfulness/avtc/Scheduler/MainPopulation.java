package org.heartfulness.avtc.Scheduler;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Main;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class MainPopulation {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CallerRepository callerRepository;

    @Autowired
    private CallService callService;

    @Autowired
    private MainRepository mainRepository;

    @Scheduled(fixedDelay = 86400000)
    public void populateMain() {
        Main main = new Main();
        Date date = Date.valueOf(LocalDate.now());
        int numberOfCallsToday = this.callService.countCallsInADay(date);
        List<Agent> agents = this.agentRepository.findAll();
        int totalMissedCalls = 0;
        for (Agent agent : agents) {
            totalMissedCalls += agent.getMissedToday();
        }
    }



}
