package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="department")
public class Department extends BaseEntity{


    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Service.class)
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Language.class)
    private Language language;

    @ManyToMany(mappedBy = "departments")
    Set<Agent> agents;

    public void addAgent(Agent agent) {
        this.agents.add(agent);
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }
}
