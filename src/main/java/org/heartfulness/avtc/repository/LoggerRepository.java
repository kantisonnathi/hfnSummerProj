package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.enums.LogEvent;
import org.heartfulness.avtc.model.Logger;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface LoggerRepository extends Repository<Logger,Long> {
    public void save(Logger logger);
   List<Logger> getByLogEventAndAgent(LogEvent logEvent,Agent agent);
}
