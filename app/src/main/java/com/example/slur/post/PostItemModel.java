package com.example.slur.post;

import java.io.Serializable;

public class PostItemModel implements Serializable {

    private int post_id, state, user_id;
    private String writer, title, place, play_date, pay, content, post_date;
    private String[] need_position, position_state;

    public PostItemModel(int post_id, int user_id, String writer, String title, String place, String play_date, int state, String[] need_position, String pay, String content, String[] position_state, String post_date){
        this.post_id = post_id;
        this.writer = writer;
        this.title = title;
        this.place = place;
        this.play_date = play_date;
        this.state = state;
        this.need_position = need_position;
        this.pay = pay;
        this.content = content;
        this.post_date = post_date;
        this.position_state = position_state;
        this.user_id = user_id;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlay_date() {
        return play_date;
    }

    public void setPlay_date(String play_date) {
        this.play_date = play_date;
    }

    public String[] getNeed_position() {
        return need_position;
    }

    public void setNeed_position(String[] need_position) {
        this.need_position = need_position;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
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

    public String[] getPosition_state() {
        return position_state;
    }

    public void setPosition_state(String[] position_state) {
        this.position_state = position_state;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
