/*
package org.heartfulness.avtc.model;

import javax.persistence.*;

@Entity
public class Skill {

    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Agent.class)
    @JoinColumn(name="agent_id")
    private Agent agent;


    public void setAgent(Agent agent) {
        this.agent=agent;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setId(Long id) {
        this.id=id;
    }

    @Id
    public Long getId() {
        return id;
    }


    @Column(name="skill")
    private String skill;


    public void setSkill(String skill) {
        this.skill=skill;
    }

    public String getSkill() {
        return skill;
    }
}
*/
