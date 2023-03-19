package com.app.pcestimate.datamodel;

public class ReplayInfo {
    String content;
    int password;

    public ReplayInfo(String content, int password) {
        this.content = content;
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
