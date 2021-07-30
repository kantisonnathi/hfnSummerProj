package org.heartfulness.avtc.form;

import org.heartfulness.avtc.model.Agent;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AgentForm {
    private Agent agent;
    
    private String dob;
    
    private String certifiedDate;

    private String trainingDate;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCertifiedDate() {
        return certifiedDate;
    }

    public void setCertifiedDate(String certifiedDate) {
        this.certifiedDate = certifiedDate;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Date convertToDate(String da) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateForm = LocalDate.parse(da, dateFormatter);
        return Date.valueOf(dateForm);
    }

    public Date convertedDOB() {
        return convertToDate(dob);
    }

    public Date convertedCertifiedDate() {
        return convertToDate(certifiedDate);
    }

    public Date convertedTrainingDate() {
        return convertToDate(trainingDate);
    }
}
