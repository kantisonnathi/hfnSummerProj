package org.heartfulness.avtc;

public class User {

    private String contactNumber; //mandatory
    private String name; //mandatory
    private String countryCode; //mandatory
    private Integer extension; //mandatory
    private String email;
    private String altContactNumber;
    private String altCountryCode; //mandatory if altContact number is filled
    private Integer panel_access;
    private Integer receivesCalls;
    private String contactType;

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getAltCountryCode() {
        return altCountryCode;
    }

    public void setAltCountryCode(String altCountryCode) {
        this.altCountryCode = altCountryCode;
    }

    public Integer getPanel_access() {
        return panel_access;
    }

    public void setPanel_access(Integer panel_access) {
        this.panel_access = panel_access;
    }

    public Integer getReceivesCalls() {
        return receivesCalls;
    }

    public void setReceivesCalls(Integer receivesCalls) {
        this.receivesCalls = receivesCalls;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
}
