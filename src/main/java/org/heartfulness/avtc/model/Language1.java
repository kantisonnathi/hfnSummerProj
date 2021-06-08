package org.heartfulness.avtc.model;

import javax.persistence.*;

@Entity
public class Language1 extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Caller.class)
    @JoinColumn(name = "caller_id")
    private Caller caller;

    public Caller getCaller() {
        return caller;
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
    }

}
