package org.heartfulness.avtc.model;


import javax.persistence.*;

@Entity
@Table(name = "calls")
public class Call extends BaseEntity {


    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Caller.class)
    @JoinColumn(name = "caller_id")
    private Caller caller;
}
