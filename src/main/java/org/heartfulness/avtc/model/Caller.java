package org.heartfulness.avtc.model;

import org.heartfulness.avtc.model.enums.CallerAgeGroup;
import org.heartfulness.avtc.model.enums.CallerCategory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "caller")
public class Caller extends BaseEntity {

    @Column(name = "allottedID")
    private String allottedID;

    @Column(name = "level")
    private Integer level;

    @Column(name = "name")
    private String name;

    @Column(name="Whatsapp_No")
    private String whatsappNumber;

    @Column(name="Relationship")
    private String relationship;

    @Column(name="email")
    private String email;

    @Column(name = "pincode")
    private String pincode;

    @Column(name="employment")
    private String employment;

    @OneToMany(mappedBy = "caller", fetch= FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<Call> calls;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "location")
    private String location;

    @Column(name = "age_group")
    @Enumerated(EnumType.STRING)
    private CallerAgeGroup ageGroup;

    @Column(name = "employment_status")
    private String employmentStatus;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "educational_Qualification")
    private String educationalStatus;

    @Column(name="marital_remarks")
    private String maritalRemarks;

    @Column(name = "educational_remarks")
    private String educationalRemarks;

    @Column(name="saved")
    private Boolean saved;

    @Column(name="Language1")
    private String language1;

    @Column(name = "Language2")
    private String language2;

    @Column(name="additional_remarks")
    private String additionalRemarks;

    @Column(name="total_calls")
    private Integer totalCalls;

    @Column(name="attended_Calls")
    private Integer attendedCalls;

    @Column(name = "escalations")
    private Integer escalations;

    @Column(name="Closures")
    private Integer closures;

    @Column(name="stat_1")
    private Integer stat1;

    @Column(name="stat_2")
    private Integer stat2;

    @Column(name="stat_3")
    private Integer stat3;

    @Column(name="customer_blocked")
    private Boolean customer_blocked;

    @Column(name="history")
    private String history; //Todo:write code to update from prev call

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private CallerCategory category;

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getMaritalRemarks() {
        return maritalRemarks;
    }

    public void setMaritalRemarks(String maritalRemarks) {
        this.maritalRemarks = maritalRemarks;
    }

    public String getEducationalRemarks() {
        return educationalRemarks;
    }

    public void setEducationalRemarks(String educationalRemarks) {
        this.educationalRemarks = educationalRemarks;
    }

    public String getLanguage1() {
        return language1;
    }

    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public String getLanguage2() {
        return language2;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public String getAdditionalRemarks() {
        return additionalRemarks;
    }

    public void setAdditionalRemarks(String additionalRemarks) {
        this.additionalRemarks = additionalRemarks;
    }

    public Integer getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(Integer totalCalls) {
        this.totalCalls = totalCalls;
    }

    public Integer getAttendedCalls() {
        return attendedCalls;
    }

    public void setAttendedCalls(Integer attendedCalls) {
        this.attendedCalls = attendedCalls;
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

    public Integer getStat1() {
        return stat1;
    }

    public void setStat1(Integer stat1) {
        this.stat1 = stat1;
    }



    public Boolean getCustomer_blocked() {
        return customer_blocked;
    }

    public void setCustomer_blocked(Boolean customer_blocked) {
        this.customer_blocked = customer_blocked;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }


    @ManyToMany
    @JoinTable(
            name = "caller_language",
            joinColumns = @JoinColumn(name = "language_id"),
            inverseJoinColumns = @JoinColumn(name = "caller_id"))
    private Set<Language> languages;

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

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
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

    public Boolean isSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    @Override
    public String toString() {
        return "contact number: " + contactNumber;
    }

    public Integer getStat2() {
        return stat2;
    }

    public void setStat2(Integer stat2) {
        this.stat2 = stat2;
    }

    public Integer getStat3() {
        return stat3;
    }

    public void setStat3(Integer stat3) {
        this.stat3 = stat3;
    }

}
