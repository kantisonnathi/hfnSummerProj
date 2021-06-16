package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "team")
public class Team extends BaseEntity{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Agent manager;

    @OneToMany(mappedBy = "team",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private List<Agent> agents;
}
