package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.ScheduleException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleExceptionRepository extends JpaRepository<ScheduleException, Long> {
}
