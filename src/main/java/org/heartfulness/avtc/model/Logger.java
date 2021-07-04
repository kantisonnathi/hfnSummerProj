package org.heartfulness.avtc.model;

import org.heartfulness.avtc.model.enums.LogEvent;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "agent_activity_log")
public class Logger extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Enumerated(EnumType.ORDINAL)
    private LogEvent logEvent;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public LogEvent getLogEvent() {
        return logEvent;
    }

    public void setLogEvent(LogEvent logEvent) {
        this.logEvent = logEvent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
