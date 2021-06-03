package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "caller")
public class Caller extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "avg_rating")
    private Integer averageRating;

    @OneToMany(mappedBy = "caller",fetch= FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Call> calls;
}
