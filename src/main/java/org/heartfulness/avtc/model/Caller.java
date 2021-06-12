package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "caller")
public class Caller extends BaseEntity {

    @Column(name = "allottedID")
    private String allottedID;

    public String getAllottedID() {
        return allottedID;
    }

    public void setAllottedID(String allottedID) {
        this.allottedID = allottedID;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CallerAgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(CallerAgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Boolean getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(Boolean employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducationalStatus() {
        return educationalStatus;
    }

    public void setEducationalStatus(String educationalStatus) {
        this.educationalStatus = educationalStatus;
    }

    public CallerCategory getCategory() {
        return category;
    }

    public void setCategory(CallerCategory category) {
        this.category = category;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    @Column(name = "level")
    private Integer level;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "caller",fetch= FetchType.EAGER,cascade=CascadeType.ALL)
    private Set<Call> calls;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "location")
    private String location;

    @Column(name = "age_group")
    @Enumerated(EnumType.STRING)
    private CallerAgeGroup ageGroup;
    /*
    * 0-14 -> 1
    * 15-47 -> 2
    * 48-63 -> 3
    * 64+ -> 4
    * */

    @Column(name = "employment_status")
    private Boolean employmentStatus;

    @Column(name = "contact_number")
    private String contactNumber;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "educational_status")
    private String educationalStatus;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private CallerCategory category;
    /*
    *Individuals from COVID affected families
    *Caregivers/Front line workers
    *Well-wishers who wish to help covid affected families
    * Survivors  of Trauma
    Individuals with Suicidal tendencies
    */

    @ManyToMany
    @JoinTable(
            name = "caller_language",
            joinColumns = @JoinColumn(name = "language_id"),
            inverseJoinColumns = @JoinColumn(name = "caller_id"))
    private Set<Language> languages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Call> getCalls() {
        return calls;
    }

    public void setCalls(Set<Call> calls) {
        this.calls = calls;
    }

    public void addCall(Call call) {
        if (calls == null) {
            calls = new HashSet<>();
        }
        calls.add(call);
    }
}
