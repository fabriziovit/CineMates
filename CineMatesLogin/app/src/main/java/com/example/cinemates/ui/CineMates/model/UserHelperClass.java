package com.example.cinemates.ui.CineMates.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

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

    private String getDefinedValue(String s) {
        if (s != null) {
            return s;
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (uid != null) {
            sb.append(uid);
        }
        if (username != null) {
            sb.append(" username : ");
            sb.append(username);
            sb.append(",");
        }
        if (email != null) {
            sb.append(", ");
            sb.append(email);
            sb.append(",");
        }
        if (imageUrl != null) {
            sb.append(" imageUrl : ");
            sb.append(imageUrl);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserHelperClass)) {
            return false;
        }
        UserHelperClass userHelperClass = (UserHelperClass) obj;
        return Objects.equals(username, userHelperClass.username)
                && Objects.equals(uid, userHelperClass.uid)
                && Objects.equals(email, userHelperClass.email)
                && Objects.equals(imageUrl, userHelperClass.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, username, email, imageUrl);
    }
}
