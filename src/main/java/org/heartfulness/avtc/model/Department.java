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

    @OneToMany(mappedBy = "department")
    Set<AgentDepartment> agents;
}
