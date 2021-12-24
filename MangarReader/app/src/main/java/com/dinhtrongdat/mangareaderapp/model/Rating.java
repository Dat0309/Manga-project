package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;

public class Rating implements Serializable {
    private String uid;
    private String mangaName;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String uid, String mangaName, String rateValue, String comment) {
        this.uid = uid;
        this.mangaName = mangaName;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
