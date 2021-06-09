package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="department")
public class Department extends BaseEntity{
    @OneToMany(mappedBy = "department",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Agent> agents;
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Service.class)
    private Service service;
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Language.class)
    private Language language;
    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }
}
