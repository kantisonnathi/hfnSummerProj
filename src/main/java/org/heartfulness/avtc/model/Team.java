package org.heartfulness.avtc.model;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "team",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Agent> agents;

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
            agent.setTeam(this);
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
