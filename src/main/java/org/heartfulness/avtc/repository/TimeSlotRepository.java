package org.heartfulness.avtc.repository;

import org.heartfulness.avtc.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

}
