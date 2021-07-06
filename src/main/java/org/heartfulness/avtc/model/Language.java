package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Language extends BaseEntity {

    @OneToMany(mappedBy = "language",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Department> Departments;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "languages")
    private Set<Caller> callers;

    @OneToMany(mappedBy = "language", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Team> teams;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Caller> getCallers() {
        return callers;
    }

    public void setCallers(Set<Caller> callers) {
        this.callers = callers;
    }

    public Set<Department> getDepartments() {
        return Departments;
    }

    public void setDepartments(Set<Department> departments) {
        Departments = departments;
    }


}
