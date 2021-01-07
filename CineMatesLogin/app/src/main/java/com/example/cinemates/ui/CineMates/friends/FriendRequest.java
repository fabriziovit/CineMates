package com.example.cinemates.ui.CineMates.friends;

public class FriendRequest {

    private String uIdDestinatario;
    private String uIdMittente;
    private String timeStamp;

    public FriendRequest(){}

    public FriendRequest(String uIdDestinatario, String uIdMittente, String timeStamp) {
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

