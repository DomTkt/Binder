package com.example.data;

import java.util.Date;

/**
 * Created by iem on 03/05/2017.
 */

public class Message {
    private String id;
    private Date time;
    private User userSender;
    private String content;

    public Message(String id, Date time, User userSender, String content) {
        this.id = id;
        this.time = time;
        this.userSender = userSender;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
