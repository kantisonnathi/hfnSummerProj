package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "caller")
public class Caller extends BaseEntity {

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Integer averageRating) {
        this.averageRating = averageRating;
    }

    public Set<Call> getCalls() {
        return calls;
    }

    public void setCalls(Set<Call> calls) {
        this.calls = calls;
    }

    @Column(name = "avg_rating")
    private Integer averageRating;

    @OneToMany(mappedBy = "caller",fetch= FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Call> calls;
}
