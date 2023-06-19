package com.mayihavek.myapplication.entity;

public class Message {
    private String sender;
    private String message;
    private int id;

    public Message(String sender, String message,int id) {
        this.sender = sender;
        this.message = message;
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public int getId(){
        return id;
    }
}

