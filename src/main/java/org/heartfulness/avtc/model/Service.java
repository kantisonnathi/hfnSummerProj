package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Service extends BaseEntity {

    @OneToMany(mappedBy = "service",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Department> departments;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams;

    public void addDepartment(Department department) {
        if (this.departments == null) {
            departments = new HashSet<>();
        }
        departments.add(department);
    }

    public void removeDepartment(Department department) {
        if (this.departments != null) {
            departments.remove(department);
        }
    }

    public void addTeam(Team team) {
        if (this.teams == null) {
            teams = new HashSet<>();
        }
        teams.add(team);
    }

    public void removeTeam(Team team) {
        if (this.teams != null) {
            teams.remove(team);
        }
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
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
