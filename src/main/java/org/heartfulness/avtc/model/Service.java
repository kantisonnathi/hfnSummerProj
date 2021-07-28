package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Service extends BaseEntity {

    @OneToMany(mappedBy = "service",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Department> Departments;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Team> teams;

    public Set<Department> getDepartments() {
        return Departments;
    }

    public void setDepartments(Set<Department> departments) {
        Departments = departments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
