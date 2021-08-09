package org.heartfulness.avtc.model;

import com.google.api.client.util.DateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Time;
import java.time.LocalTime;

@Entity
public class Schedule extends BaseEntity {

    private DateTime startTime;

    private DateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
