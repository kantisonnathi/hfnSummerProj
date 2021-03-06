package org.heartfulness.avtc.model;


import org.heartfulness.avtc.model.enums.CallCategory;
import org.heartfulness.avtc.model.enums.CallStatus;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "calls")
public class Call extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Agent.class)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    @Enumerated(EnumType.ORDINAL)
    private CallCategory category;

    @Column(name = "location")
    private String location;

    @Column(name="URL")
    private String url;

    @Column(name="uid", unique = true)
    private String uid;

    @Column(name="saved")
    private Boolean saved;

    @Column(name="duration")
    private String duration;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Caller.class)
    @JoinColumn(name = "caller_id")
    private Caller caller;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private CallStatus callStatus;

    @Column(name = "review_flag")
    private Boolean reviewFlag;

    @OneToMany(mappedBy = "leasedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Agent> leasing;

    @Column(name="escalation")
    private Boolean escalation;

    public Boolean getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(Boolean reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public Boolean getEscalation() {
        return escalation;
    }

    public void setEscalation(Boolean escalation) {
        this.escalation = escalation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CallCategory getCategory() {
        return category;
    }

    public void setCategory(CallCategory category) {
        this.category = category;
    }

    public CallStatus getStatus() {
        return callStatus;
    }

    public void setStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public Set<Agent> getLeasing() {
        return leasing;
    }

    public void setLeasing(Set<Agent> leasing) {
        this.leasing = leasing;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean isSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Caller getCaller() {
        return caller;
    }

    public void setCaller(Caller caller) {
        caller.addCall(this);
        this.caller = caller;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public void addLeasedAgent(Agent agent) {
        if (this.leasing == null) {
            leasing = new HashSet<>();
        }
        leasing.add(agent);
    }

    public void removeLeasingAgent(Agent agent) {
        if (this.leasing != null) {
            leasing.remove(agent);
        }
    }

}


