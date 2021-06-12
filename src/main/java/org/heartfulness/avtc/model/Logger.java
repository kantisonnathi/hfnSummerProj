package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "agent_activity_log")
public class Logger extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Enumerated(EnumType.ORDINAL)
    private LogEvent logEvent;

    @Column(name = "timestamp")
    private String timestamp;
}
