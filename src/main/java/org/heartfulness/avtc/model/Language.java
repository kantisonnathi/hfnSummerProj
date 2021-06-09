package org.heartfulness.avtc.model;

import javax.persistence.*;

@Entity
public class Language extends BaseEntity {

    @Column(name="name")
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
