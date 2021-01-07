package com.example.slur.post;

import java.io.Serializable;

public class PlayerPostItemModel implements Serializable {

    private int post_id, state, user_id;
    private String writer, title, content, post_date;
    private String[] place, need_position;
    private String pay; //kjy

    public PlayerPostItemModel(int post_id, String writer, String title, int state, String[] place, String[] need_position, int user_id, String content, String post_date, String pay){
        this.post_id = post_id;
        this.writer = writer;
        this.title = title;
        this.place = place;
        this.state = state;
        this.need_position = need_position;
        this.user_id = user_id;
        this.content = content;
        this.post_date = post_date;
        this.pay = pay;//kjy
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.substring(0,9);
        //10글자만 저장
    }

    public String[] getPlace() {
        return place;
    }

    public void setPlace(String[] place) {
        this.place = place;
    }

    public String[] getNeed_position() {
        return need_position;
    }

    public void setNeed_position(String[] need_position) {
        this.need_position = need_position;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPay() {
        return pay;
    }//kjy

    public void setPay(String pay) {
        this.pay = pay;
    }//kjy
}
