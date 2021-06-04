package org.heartfulness.avtc.model;


import javax.persistence.*;

@Entity
@Table(name = "calls")
public class Call extends BaseEntity {


    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Caller.class)
    @JoinColumn(name = "caller_id")
    private Caller caller;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private String description;
}
