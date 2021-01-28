package com.example.cinemates.ui.CineMates.friends.model;

import com.google.firebase.firestore.FieldValue;

public class Friends {

    private String uId;
    private FieldValue timeStamp;

    public Friends() {
    }

    public Friends(String uId, FieldValue timeStamp) {
        this.uId = uId;
        this.timeStamp = timeStamp;
    }
    
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public FieldValue getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(FieldValue timeStamp) {
        this.timeStamp = timeStamp;
    }
}
