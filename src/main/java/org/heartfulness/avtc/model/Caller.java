package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "caller")
public class Caller extends BaseEntity {

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

    @OneToMany(mappedBy = "caller", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Language1> languages;

    @Column(name = "age_group")
    private Integer ageGroup;
    /*
    * 0-14 -> 1
    * 15-47 -> 2
    * 48-63 -> 3
    * 64+ -> 4
    * */

    @Column(name = "employment_status")
    private Boolean employmentStatus;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "educational_status")
    private String educationalStatus;

    @Column(name = "category")
    private String category;
    /*
    *Individuals from COVID affected families
    *Caregivers/Front line workers
    *Well-wishers who wish to help covid affected families
    * Survivors  of Trauma
    Individuals with Suicidal tendencies
    */

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
