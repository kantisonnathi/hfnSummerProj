package org.heartfulness.avtc.model;



import org.heartfulness.avtc.model.enums.AgentRole;
import org.heartfulness.avtc.model.enums.AgentStatus;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "agent")
public class Agent extends BaseEntity {

    private String password;

    @Column(name = "contact_number",unique = true,nullable = false)
    private String contactNumber; //mandatory

    @Column(name = "name")
    private String name; //mandatory

    @Column(name = "email")
    private String email;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="pincode")
    private String pincode;

    @Column(name="dob")
    private Date DOB;

    @Column(name = "educational_level")
    private String educationalLevel;

    @Column(name = "acad_degree")
    private String acadDegree;

    @Column(name="edu_desc")
    private String eduDesc;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "organization")
    private String organization;

    @Column(name = "designation")
    private String designation;

    @Column(name="work_exp")
    private String workExperience;

    @Column(name = "skills")
    private String skills;

    @Column(name = "comm_service")
    private String commService;

    @Column(name="psychology_flag")
    private String psychologyFlag;

    @Column(name = "relevant_exp")
    private Integer relevantExperience;

    @Column(name="certified_date")
    private Date certifiedDate;

    @Column(name="last_training_date")
    private Date lastTrainingDate;

    @Column(name = "training_scores")
    private Double trainingScores;

    @Column(name = "performance_rating")
    private Double performanceRating;

    @Column(name = "escalations")
    private Integer escalations;

    @Column(name="closures")
    private Integer closures;

    @Column(name="stat1")
    private Double stat1;

    @Column(name="stat2")
    private Double stat2;

    @Column(name="stat3")
    private Double stat3;

    @Column(name = "missed")
    private Integer missed;

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

    @Column(name = "end_time")
    private Time endTime;

    public java.sql.Time getEndTime() {
        return endTime;
    }

    public void setEndTime(java.sql.Time endTime) {
        this.endTime = endTime;
    }

    @ManyToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Department> departments;

    @OneToMany(mappedBy = "agent",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Logger> loggerSet;

    @OneToMany(mappedBy = "agent", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "agent", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Call> calls;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Team.class)
    private Set<Team> teams;

    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Team teamManaged;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "leased_by")
    private Call leasedBy;

    @OneToMany(mappedBy = "agent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ScheduleException> scheduleExceptions;

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<ScheduleException> getScheduleExceptions() {
        return scheduleExceptions;
    }

    public void setScheduleExceptions(Set<ScheduleException> scheduleExceptions) {
        this.scheduleExceptions = scheduleExceptions;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Call getLeasedBy() {
        return leasedBy;
    }

    public void setLeasedBy(Call leasedBy) {
        this.leasedBy = leasedBy;
    }

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

    public Set<Team> getTeam() {
        return teams;
    }

    public void setTeam(Set<Team> teams) {
        this.teams = teams;
    }

    public void addTeam(Team team) {
        if (this.teams == null) {
            teams = new HashSet<>();
        }
        teams.add(team);
    }

    public void removeTeam(Team team) {
        if (teams == null) {
            teams = new HashSet<>();
        }
        teams.remove(team);
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

    public Set<Call> getCalls() {
        return calls;
    }

    public void setCalls(Set<Call> calls) {
        this.calls = calls;
    }

    public void addCall(Call call) {
        if (this.calls == null) {
            calls = new HashSet<>();
        }
        calls.add(call);
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Integer getMissed() {
        return missed;
    }

    public void setMissed(Integer missed) {
        this.missed = missed;
    }

    public void addMissed() {
        this.missed++;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    public String getAcadDegree() {
        return acadDegree;
    }

    public void setAcadDegree(String acadDegree) {
        this.acadDegree = acadDegree;
    }

    public String getEduDesc() {
        return eduDesc;
    }

    public void setEduDesc(String eduDesc) {
        this.eduDesc = eduDesc;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCommService() {
        return commService;
    }

    public void setCommService(String commService) {
        this.commService = commService;
    }

    public String getPsychologyFlag() {
        return psychologyFlag;
    }

    public void setPsychologyFlag(String psychologyFlag) {
        this.psychologyFlag = psychologyFlag;
    }

    public Integer getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(Integer relevantExperience) {
        this.relevantExperience = relevantExperience;
    }

    public Date getCertifiedDate() {
        return certifiedDate;
    }

    public void setCertifiedDate(Date certifiedDate) {
        this.certifiedDate = certifiedDate;
    }

    public Date getLastTrainingDate() {
        return lastTrainingDate;
    }

    public void setLastTrainingDate(Date lastTrainingDate) {
        this.lastTrainingDate = lastTrainingDate;
    }

    public Double getTrainingScores() {
        return trainingScores;
    }

    public void setTrainingScores(Double trainingScores) {
        this.trainingScores = trainingScores;
    }

    public Double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(Double performanceRating) {
        this.performanceRating = performanceRating;
    }

    public Integer getEscalations() {
        return escalations;
    }

    public void setEscalations(Integer escalations) {
        this.escalations = escalations;
    }

    public Integer getClosures() {
        return closures;
    }

    public void setClosures(Integer closures) {
        this.closures = closures;
    }

    public Double getStat1() {
        return stat1;
    }

    public void setStat1(Double stat1) {
        this.stat1 = stat1;
    }

    public Double getStat2() {
        return stat2;
    }

    public void setStat2(Double stat2) {
        this.stat2 = stat2;
    }

    public Double getStat3() {
        return stat3;
    }

    public void setStat3(Double stat3) {
        this.stat3 = stat3;
    }
}

