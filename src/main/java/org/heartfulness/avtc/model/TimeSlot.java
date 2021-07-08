package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
public class TimeSlot extends BaseEntity {

    private Time startTime;

    private Time endTime;

    @OneToMany(mappedBy = "slot", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ScheduleException> scheduleExceptions;

    public void addScheduleException(ScheduleException scheduleException) {
        if (this.scheduleExceptions == null) {
            scheduleExceptions = new HashSet<>();
        }
        this.scheduleExceptions.add(scheduleException);
    }

    public Set<ScheduleException> getScheduleExceptions() {
        return scheduleExceptions;
    }

    public void setScheduleExceptions(Set<ScheduleException> scheduleExceptions) {
        this.scheduleExceptions = scheduleExceptions;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
