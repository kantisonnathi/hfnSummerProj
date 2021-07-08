package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ScheduleException extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Agent.class)
    private Agent agent;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = TimeSlot.class)
    private TimeSlot slot;

    private Date date;

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
