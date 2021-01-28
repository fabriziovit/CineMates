package com.example.cinemates.ui.CineMates.model;

public class ReviewModel {
    private String uid;
    private String review;
    private int star;

    public ReviewModel(String uid, String review, int star) {
        this.uid = uid;
        this.review = review;
        this.star = star;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
