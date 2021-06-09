package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Language extends BaseEntity {

    @OneToMany(mappedBy = "language",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Department> Departments;

    @Column(name="name")
    private String language;

    @ManyToMany(mappedBy = "languages")
    private Set<Caller> callers;

    public Set<Department> getDepartments() {
        return Departments;
    }

    public void setDepartments(Set<Department> departments) {
        Departments = departments;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
