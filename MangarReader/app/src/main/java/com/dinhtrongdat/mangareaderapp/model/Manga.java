package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Lớp Manga định nghĩa các truyện manga
 */
public class Manga implements Serializable {
    private String Name, Image, Category, Description, Author, Backdrop, Rate;
    List<Chapter> Chapters;

    public Manga() {
    }

    public Manga(String name, String image, String category, String description, String author, String backdrop, List<Chapter> chapters, String rate) {
        Name = name;
        Image = image;
        Category = category;
        Description = description;
        Chapters = chapters;
        Author = author;
        Backdrop = backdrop;
        Rate = rate;
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

    public String getRate() { return  Rate;}

    public void setRate(String rate) { Rate = rate;}
}
