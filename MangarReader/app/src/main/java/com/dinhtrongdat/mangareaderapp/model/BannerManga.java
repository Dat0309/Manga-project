package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;
import java.util.List;

public class BannerManga implements Serializable {
    private String Name, Image, Category;
    private List<Chapter> Chapters;

    public BannerManga() {
    }

    public BannerManga(String name, String image, String category, List<Chapter> chapters) {
        Name = name;
        Image = image;
        Category = category;
        Chapters = chapters;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public List<Chapter> getChapters() {
        return Chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        Chapters = chapters;
    }
}
