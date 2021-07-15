package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.ScheduleException;
import org.heartfulness.avtc.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Set;

@Repository
public interface ScheduleExceptionRepository extends JpaRepository<ScheduleException, Long> {

    Set<ScheduleException> findAllByAgent(Agent agent);

    ScheduleException findByDateAndAgentAndSlot(Date date, Agent agent, TimeSlot timeSlot);

}
