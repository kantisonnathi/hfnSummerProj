package org.heartfulness.avtc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Main {

    private String id;
    private Integer totalNumber;
    /*
    * for calls - total number of calls
    * for agents - total number of agents
    * for callers - total number of callers
    * for teams - total number of teams*/
    private Integer totalMissed;
    /*
    * calls - total amount of missed calls
    * agents - total amount of calls missed by that agent in total
    * callers - total amount of calls missed from this caller
    * teams - total amount of calls missed by this team*/


    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }
}
