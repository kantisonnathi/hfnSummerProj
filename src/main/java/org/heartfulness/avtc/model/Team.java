package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "manager", referencedColumnName = "id")
    private Agent manager;

    @ManyToMany(mappedBy = "teams",fetch=FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<Agent> agents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    private Language language;

    @ManyToMany(mappedBy = "teams", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<TimeSlot> timeSlots = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Service.class)
    private Service service;

    private String description;

    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.add(timeSlot);
    }

    public void removeTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.remove(timeSlot);
    }

    public Set<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setTimeSlots(Set<TimeSlot> timeSlots) {
        for (TimeSlot timeSlot : timeSlots) {
            timeSlot.addTeam(this);
        }
        this.timeSlots = timeSlots;
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

    public void addAgent(Agent agent) {
        agent.addTeam(this);
        this.agents.add(agent);
    }

    public void removeAgent(Agent agent) {
        this.agents.remove(agent);
    }


}
