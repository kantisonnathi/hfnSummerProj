/*
package org.heartfulness.avtc.model;

import javax.persistence.*;

@Entity
public class Language {
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    @Column(name="language")
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
*/
