package com.app.pcestimate.datamodel;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDataModel implements Serializable {

    String id;
    String title;
    String content;
    String password;
    ArrayList<Replies> replies;
    ArrayList<String> pictures;

    public PostDataModel() {
    }

    ;

    public PostDataModel(String title, String content, String password, ArrayList<Replies> replies, ArrayList<String> pictures) {
        this.title = title;
        this.content = content;
        this.password = password;
        this.replies = replies;
        this.pictures = pictures;
    }

//    public PostDataModel(String title, String content, String password, ArrayList<String> replies) {
//        this.title = title;
//        this.content = content;
//        this.password = password;
//        this.replies = replies;
//    }

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

    public ArrayList<Replies> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Replies> replies) {
        this.replies = replies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }
}


