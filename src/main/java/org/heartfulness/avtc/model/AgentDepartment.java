package org.heartfulness.avtc.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AgentDepartment implements Serializable {

    @EmbeddedId
    AgentDepartmentKey id;

    @ManyToOne
    @MapsId("agentId")
    private Agent agent;

    @ManyToOne
    @MapsId("departmentId")
    private Department department;

    public AgentDepartmentKey getId() {
        return id;
    }

    public void setId(AgentDepartmentKey id) {
        this.id = id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
