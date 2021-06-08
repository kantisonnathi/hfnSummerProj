package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="department")
public class Department extends BaseEntity{
    @Column(name="language")
    private String Language;
    @Column(name="service")
    private String Service;
    @OneToMany(mappedBy = "department",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Agent> agents;

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }
}
