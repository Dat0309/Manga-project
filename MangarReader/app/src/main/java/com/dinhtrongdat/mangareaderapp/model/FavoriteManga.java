package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Lớp Favorite định nghĩa các truyện được yêu thích của người dùng
 */
public class FavoriteManga implements Serializable {

    private String Name, Image, Category, Description, Author, Backdrop, Uid;
    List<Chapter> Chapters;

    public FavoriteManga() {
    }

    public FavoriteManga(String name, String image, String category, String description, String author, String backdrop, String uid, List<Chapter> chapters) {
        Name = name;
        Image = image;
        Category = category;
        Description = description;
        Author = author;
        Backdrop = backdrop;
        Uid = uid;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getBackdrop() {
        return Backdrop;
    }

    public void setBackdrop(String backdrop) {
        Backdrop = backdrop;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public List<Chapter> getChapters() {
        return Chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        Chapters = chapters;
    }
}
