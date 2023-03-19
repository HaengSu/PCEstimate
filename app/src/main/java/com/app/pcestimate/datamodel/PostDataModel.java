package com.app.pcestimate.datamodel;

import android.net.Uri;

import java.util.ArrayList;

public class PostDataModel {

    String title;
    String content;
    String password;
    ArrayList<ReplayInfo> replies;
//    ArrayList<Uri> pictures;

    public PostDataModel() {
    };

    public PostDataModel(String title, String content, String password, ArrayList<ReplayInfo> replies) {
        this.title = title;
        this.content = content;
        this.password = password;
        this.replies = replies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ReplayInfo> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<ReplayInfo> replies) {
        this.replies = replies;
    }

//    public ArrayList<Uri> getPictures() {
//        return pictures;
//    }
//
//    public void setPictures(ArrayList<Uri> pictures) {
//        this.pictures = pictures;
//    }
}

