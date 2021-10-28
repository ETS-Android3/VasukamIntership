package com.saggy.vasukaminternship.models;

public class Chat_Model {
    String key;
    String uid;

    public Chat_Model(){}

    public Chat_Model(String key, String uid) {
        this.key =key;
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }
}
