package org.heartfulness.avtc.model;



import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
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
    @Enumerated(EnumType.STRING)
    private AgentRole role;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private AgentStatus status; //online offline busy

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name="gender")

    private char gender;

    @Column(name="level",length =1)
    private int level;

    @ManyToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "agent_department",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id"))
    private Set<Department> departments;

    @OneToMany(mappedBy = "agent",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Logger> loggerSet;

    @OneToMany(mappedBy = "agent", fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Schedule> schedules;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Team.class)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne(mappedBy = "manager")
    private Team teamManaged;

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

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

    public AgentRole getRole() {
        return role;
    }

    public void setRole(AgentRole role) {
        this.role = role;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeamManaged() {
        return teamManaged;
    }

    public void setTeamManaged(Team teamManaged) {
        this.teamManaged = teamManaged;
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
       if(name.equals(""))
       {
           return false;
       }
        return true;
    }

    public void addSchedule(Schedule schedule) {
        if (schedules == null) {
            schedules = new HashSet<>();
        }
        this.schedules.add(schedule);
    }

    public Set<Logger> getLoggerSet() {
        return loggerSet;
    }

    public void setLoggerSet(Set<Logger> loggerSet) {
        this.loggerSet = loggerSet;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public void addDepartment(Department department) {
        department.addAgent(this);
        this.departments.add(department);
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

}

