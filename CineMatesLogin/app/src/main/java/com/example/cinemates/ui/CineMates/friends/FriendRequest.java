package com.example.cinemates.ui.CineMates.friends;

import com.google.firebase.firestore.FieldValue;

public class FriendRequest {

    private String uIdDestinatario;
    private String uIdMittente;
    private FieldValue timeStamp;

    public FriendRequest(){}

    public FriendRequest(String uIdDestinatario, String uIdMittente, FieldValue timeStamp) {
        this.uIdDestinatario = uIdDestinatario;
        this.uIdMittente = uIdMittente;
        this.timeStamp = timeStamp;
    }

    public String getuIdDestinatario() {
        return uIdDestinatario;
    }

    public void setuIdDestinatario(String uIdDestinatario) {
        this.uIdDestinatario = uIdDestinatario;
    }

    public String getuIdMittente() {
        return uIdMittente;
    }

    public void setuIdMittente(String uIdMittente) {
        this.uIdMittente = uIdMittente;
    }

    public FieldValue getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(FieldValue timeStamp) {
        this.timeStamp = timeStamp;
    }
}

