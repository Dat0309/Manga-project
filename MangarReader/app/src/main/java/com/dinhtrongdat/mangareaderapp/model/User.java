package com.dinhtrongdat.mangareaderapp.model;

import java.io.Serializable;

/**
 * Lớp User lưu trữ thông tin của người dùng
 */
public class User implements Serializable {

    private String UserName, PassWord, Name, Avatar;

    public User() {
    }

    public User(String userName, String passWord, String name, String avatar) {
        UserName = userName;
        PassWord = passWord;
        Name = name;
        Avatar = avatar;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
