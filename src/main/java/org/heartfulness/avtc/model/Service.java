package org.heartfulness.avtc.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Service extends BaseEntity {
    @Column(name="name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
