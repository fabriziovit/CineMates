package com.example.cinemates.ui.CineMates;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserHelperClass {
    String uid;
    String email;
    String username;
    String imageUrl;

    public UserHelperClass(){

    }

    public UserHelperClass(String uid, String email, String username, String imageUrl) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String  getUid(){
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
