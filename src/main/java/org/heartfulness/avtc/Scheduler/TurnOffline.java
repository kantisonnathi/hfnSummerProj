package org.heartfulness.avtc.Scheduler;

import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LoggerRepository;
import org.heartfulness.avtc.repository.ScheduleRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@Configuration
@EnableScheduling
public class TurnOffline {
    @Autowired
    SecurityService securityService;

    private AgentRepository agentRepository;
    private ScheduleRepository scheduleRepository;
    private LoggerRepository loggerRepository;

    public TurnOffline(AgentRepository agentRepository, ScheduleRepository scheduleRepository, LoggerRepository loggerRepository) {
        this.agentRepository = agentRepository;
        this.scheduleRepository = scheduleRepository;
        this.loggerRepository = loggerRepository;
    }
    @Scheduled(fixedDelay = 10000)
    public void turnoffline() throws ParseException {
     // Agent agent=agentRepository.findByContactNumber(securityService.getUser().getPhoneNumber());
      List<Schedule> schedules=scheduleRepository.findAll();
      LocalTime time=java.time.LocalTime.now();
      Time t=Time.valueOf(time);
      String Currtime=String.valueOf(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d1 = sdf.parse(Currtime);

        for(Schedule s: schedules)
      {
          Date d2 = sdf.parse(String.valueOf(s.getEndTime()));
          long elapsed = d2.getTime() - d1.getTime();
          if(elapsed<=0)
          {
                  Agent agent=s.getAgent();
                  agent.setStatus(AgentStatus.OFFLINE);
                  agentRepository.save(agent);
                  Logger logger=new Logger();
                  LocalDateTime localDateTime=LocalDateTime.now();
                  Timestamp timestamp=Timestamp.valueOf(localDateTime);
                  logger.setTimestamp(timestamp);
                  logger.setAgent(agent);
                  logger.setLogEvent(LogEvent.SCHEDULED_OFFLINE);
                  loggerRepository.save(logger);
                  scheduleRepository.delete(s);
          }
      }

    }//Need to test
}
