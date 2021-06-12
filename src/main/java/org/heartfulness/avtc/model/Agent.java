package org.heartfulness.avtc.model;



import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "agent")
public class Agent extends BaseEntity {

    @Column(name = "contact_number",unique = true,nullable = false)
    private String contactNumber; //mandatory

    @Column(name = "name")
    private String name; //mandatory

    @Column(name = "certified")
    private Boolean certified;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status; //online offline busy

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name="gender")
    private char gender;

    @Column(name="level",length =1)
    private int level;

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getCertified() {
        return certified;
    }

    public void setCertified(Boolean certified) {
        this.certified = certified;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean validate() {
        // add any other validations that you want here
        return true;
    }

    public Set<Logger> getLoggerSet() {
        return loggerSet;
    }

    public void setLoggerSet(Set<Logger> loggerSet) {
        this.loggerSet = loggerSet;
    }

    @OneToMany
    private Set<AgentDepartment> departments;

    @OneToMany(mappedBy = "agent",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Logger> loggerSet;

    @OneToMany(mappedBy = "agent", fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Schedule> schedules;


}
