package com.app.pcestimate.datamodel;

import java.util.HashMap;

public class Replies {
    String reply;
    int replayPassword;

    public Replies(String reply, int replayPassword) {
        this.reply = reply;
        this.replayPassword = replayPassword;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getReplayPassword() {
        return replayPassword;
    }

    public void setReplayPassword(int replayPassword) {
        this.replayPassword = replayPassword;
    }
}
