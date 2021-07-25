package org.heartfulness.avtc.service;

import org.heartfulness.avtc.model.ScheduleException;
import org.heartfulness.avtc.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

public interface ScheduleExceptionService {

    Page<ScheduleException> findAllPaginatedByTeamAndDate(Date date, Team team, int pageno, int pagesize, String sortField, String sortDirection);

    void save(ScheduleException scheduleException);

    boolean findEqual(ScheduleException scheduleException);

    ScheduleException findById(Long id);

    void delete(Long entityId);

}
