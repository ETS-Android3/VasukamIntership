package com.saggy.vasukaminternship.models;

import java.io.Serializable;

public class ProfileInfo implements Serializable {
    public String name, uid, username, phoneNumber, emailId, dateOfBirth;

    public ProfileInfo(){

    }

    public ProfileInfo(String name, String uid, String username, String phoneNumber, String emailId, String dateOfBirth) {
        this.name = name;
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
