package org.heartfulness.avtc.model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
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

    @OneToMany(mappedBy = "agent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Language> languages;

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public void addLanguage(Language language) {
        if (this.languages == null) {
            languages = new HashSet<>();
        }
        languages.add(language);
    }

    @OneToMany(mappedBy = "agent",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Skill> skills;


    public Set<Skill> getSkills(){
        return skills;
    }


    public void setSkills(Set<Skill> skills){
        this.skills=skills;
    }


    public void addSkill(Skill skill) {
        if (this.skills == null)  {
            skills = new HashSet<>();
        }
        skills.add(skill);
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
        if (languages.size() < 1 || skills.size() < 1) {
            return false;
        }
        // add any other validations that you want here
        return true;
    }

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Department.class)
    private Department department;

    @OneToMany(mappedBy = "agent",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Logger> loggerSet;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

}
