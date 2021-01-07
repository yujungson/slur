package com.example.slur.chat;

public class chatData {
    int chat_id;
    int user_id;
    String message;
    String time;

    public chatData(){

    }

    public chatData(int chat_id, int user_id, String message, String time) {
        this.chat_id = chat_id;
        this.user_id = user_id;
        this.message = message;
        this.time = time;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}