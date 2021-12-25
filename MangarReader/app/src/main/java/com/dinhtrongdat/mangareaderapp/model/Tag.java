package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;

/**
 * Lớp Tag định nghĩa các thể loại truyện
 */
public class Tag implements Serializable {
    private String tag;

    public Tag() {
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
