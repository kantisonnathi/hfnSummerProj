package org.heartfulness.avtc.model;

import javax.persistence.Entity;
import java.sql.Date;

@Entity
public class Main extends BaseEntity {

    private Integer totalNumberOfCalls;

    private Integer missedCalls;

    private Integer numberOfAgents;

    private Integer numberOfTeams;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTotalNumberOfCalls() {
        return totalNumberOfCalls;
    }

    public void setTotalNumberOfCalls(Integer totalNumberOfCalls) {
        this.totalNumberOfCalls = totalNumberOfCalls;
    }

    public Integer getMissedCalls() {
        return missedCalls;
    }

    public void setMissedCalls(Integer missedCalls) {
        this.missedCalls = missedCalls;
    }

    public Integer getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(Integer numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }
}
