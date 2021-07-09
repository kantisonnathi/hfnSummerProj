package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team extends BaseEntity{

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Agent manager;

    @ManyToMany(mappedBy = "teams",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Agent> agents;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Language.class)
    private Language language;

    private Time startTime;
    //TODO: make this time slot instead of start time and end time

    private Time endTime;

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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Agent getManager() {
        return manager;
    }

    public void setManager(Agent manager) {
        this.manager = manager;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public void addAgent(Agent agent) throws Exception {
        if (this.agents == null) {
            this.agents = new HashSet<>();
        }
        if (agent.getTeam() != null) {
            throw new Exception("agent already has team");
        } else {
            agent.addTeam(this);
            this.agents.add(agent);
        }
    }

    public void removeAgent(Agent agent) {
        if (this.agents == null) {
            this.agents = new HashSet<>();
        }
        agent.setTeam(null);
        this.agents.remove(agent);
    }


}
