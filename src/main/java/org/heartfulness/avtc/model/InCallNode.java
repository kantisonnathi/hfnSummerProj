package org.heartfulness.avtc.model;

import java.util.List;

public class InCallNode {

    private String clid_raw;

    private String clid;

    private String create;

    private Integer event;

    private Integer status;

    private Integer call_state;

    private String uid;

    private String company_id;

    private String rdnis;

    private List<String> users;

    public String getClid_raw() {
        return clid_raw;
    }

    public void setClid_raw(String clid_raw) {
        this.clid_raw = clid_raw;
    }

    public String getClid() {
        return clid;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
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

