package org.heartfulness.avtc.model;

import java.util.List;

public class InCallNode {
    public InCallNode(String reference_id, String call_time, String public_ivr_id, String client_ref_id, String job_id, String id, String clid_raw, String clid, String created, Integer event, Integer status, Integer call_state, String uid, String company_id, String rdnis, List<String> users) {
        this.reference_id = reference_id;
        this.call_time = call_time;
        this.public_ivr_id = public_ivr_id;
        this.client_ref_id = client_ref_id;
        this.job_id = job_id;
        this.id = id;
        this.clid_raw = clid_raw;
        this.clid = clid;
        this.created = created;
        this.event = event;
        this.status = status;
        this.call_state = call_state;
        this.uid = uid;
        this.company_id = company_id;
        this.rdnis = rdnis;
        this.users = users;
    }

    public InCallNode() {

    }

    private String reference_id; //ok

    private String call_time; //ok

    private String public_ivr_id; //ok

    private String client_ref_id; // ok

    private String job_id;

    private String id; //ok

    private String clid_raw; //ok

    private String clid; //ok

    private String created; //ok

    private Integer event; //ok

    private Integer status; //ok

    private Integer call_state; //ok

    private String uid; //ok

    private String company_id; //ok

    private String rdnis; //ok

    private List<String> users; //ok

    public String getClid_raw() {
        return clid_raw;
    }

    public void setClid_raw(String clid_raw) {
        this.clid_raw = clid_raw;
    }

    public String getClid() {
        return clid;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public String getPublic_ivr_id() {
        return public_ivr_id;
    }

    public void setPublic_ivr_id(String public_ivr_id) {
        this.public_ivr_id = public_ivr_id;
    }

    public String getClient_ref_id() {
        return client_ref_id;
    }

    public void setClient_ref_id(String client_ref_id) {
        this.client_ref_id = client_ref_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCall_state() {
        return call_state;
    }

    public void setCall_state(Integer call_state) {
        this.call_state = call_state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getRdnis() {
        return rdnis;
    }

    public void setRdnis(String rdnis) {
        this.rdnis = rdnis;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

