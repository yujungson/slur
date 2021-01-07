package com.example.slur.chat;

import java.io.Serializable;

public class messageItemModel implements Serializable {
    private int senderId; // 사용자 아이디
    private String message; // 작성한 메시지

    public messageItemModel(int senderId, String message) {
        this.senderId = senderId;
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
