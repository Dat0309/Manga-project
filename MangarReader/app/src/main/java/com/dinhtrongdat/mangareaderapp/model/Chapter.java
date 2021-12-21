package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Lớp Chapter định nghĩa các chương của truyện
 */
public class Chapter implements Serializable {
    private List<String> Links;
    private String Name;

    public Chapter() {
    }

    public Chapter(List<String> links, String name) {
        Links = links;
        Name = name;
    }

    public List<String> getLinks() {
        return Links;
    }

    public void setLinks(List<String> links) {
        Links = links;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
