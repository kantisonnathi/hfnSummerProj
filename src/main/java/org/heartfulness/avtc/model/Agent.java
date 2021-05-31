package org.heartfulness.avtc.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "agent")
public class Agent extends BaseEntity {

    @Column(name = "contact_number")
    private String contactNumber; //mandatory

    @Column(name = "name")
    private String name; //mandatory

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "password")
    private String password;

    @Column(name = "extension")
    private Integer extension; //mandatory

    @Column(name = "email")
    private String email;

    @Column(name = "alt_contact_number")
    private String altContactNumber;

    @Column(name = "certified")
    private Boolean certified;

    @Column(name = "is_new")
    private Boolean isNew;

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
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


    public void addSkill(Skill skill){
        if(this.skills==null)
        {
            skills=new HashSet<>();

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

    public Integer getExtension() {
        return extension;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAltContactNumber() {
        return altContactNumber;
    }

    public void setAltContactNumber(String altContactNumber) {
        this.altContactNumber = altContactNumber;
    }

}
