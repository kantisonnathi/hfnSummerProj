package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Schedule;
import org.springframework.data.repository.Repository;

public interface ScheduleRepository extends Repository<Schedule, Long> {

    void save(Schedule schedule);
}
