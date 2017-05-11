package com.example.data;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by iem on 03/05/2017.
 */

public class Chat implements Serializable {
    private String id;
    private User user1;
    private User user2;
    private ArrayList<Message> conversation;

    public Chat(String id, User user1, User user2, ArrayList<Message> conversation) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.conversation = conversation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public ArrayList<Message> getConversation() {
        return conversation;
    }

    public void setConversation(ArrayList<Message> conversation) {
        this.conversation = conversation;
    }


}
