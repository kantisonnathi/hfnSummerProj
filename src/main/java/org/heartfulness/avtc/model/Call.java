package org.heartfulness.avtc.model;


import javax.persistence.*;

@Entity
@Table(name = "calls")
public class Call extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    /*
    *
    * Adjustment Disorders (Occasional Anxiety)
    * Depressive Disorders
    * Substance abuse disorder
    * Anxiety disorder
    * OCD disorder (Obsessive-Compulsive Disorder)
    * Serious Mental Illness
    *
    * */

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Caller.class)
    @JoinColumn(name = "caller_id")
    private Caller caller;

    public Caller getCaller() {
        return caller;
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
