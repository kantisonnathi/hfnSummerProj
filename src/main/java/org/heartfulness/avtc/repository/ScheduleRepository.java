package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Schedule;
import org.springframework.data.repository.Repository;

import java.sql.Time;
import java.util.List;

public interface ScheduleRepository extends Repository<Schedule, Long> {

    void save(Schedule schedule);
    List<Schedule> findAll();
    void delete(Schedule schedule);
    List<Schedule> findByAgent(Agent agent);
}
