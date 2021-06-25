package org.heartfulness.avtc.Scheduler;

import org.heartfulness.avtc.Comparator.sortByLog;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.LogEvent;
import org.heartfulness.avtc.model.Logger;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LoggerRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableScheduling
public class LogOut {

    private LoggerRepository loggerRepository;
    private AgentRepository agentRepository;

    public LogOut(LoggerRepository loggerRepository, AgentRepository agentRepository) {
        this.loggerRepository = loggerRepository;
        this.agentRepository = agentRepository;
    }

    @Scheduled(fixedDelay =86400000)
    public void Logactivity()
    {
        List<Agent> agents=agentRepository.findAll();
        for(Agent agent:agents) {
            List<Logger> loggerList = loggerRepository.getByLogEvent(LogEvent.LOGIN,agent);
            Collections.sort(loggerList, new sortByLog());
            LocalDateTime localDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);

                if (timestamp.compareTo(loggerList.get(loggerList.size()-1).getTimestamp()) + 5 >= 0) {
                    Logger add = new Logger();
                    add.setLogEvent(LogEvent.AUTOMATIC_LOGOUT);
                    add.setAgent(agent);
                    add.setTimestamp(timestamp);
                    loggerRepository.save(add);
                }

        }
    }
}
