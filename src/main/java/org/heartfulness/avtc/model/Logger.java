package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Logger extends BaseEntity {

    @ManyToMany
    @JoinTable(
            name = "agent_log",
            joinColumns = @JoinColumn(name = "agent_id"),
            inverseJoinColumns = @JoinColumn(name = "log_id"))
    private Set<Agent> agents;

    @Column(name = "status")
    private String status;

    @Column(name = "timestamp")
    private String timestamp;
}
