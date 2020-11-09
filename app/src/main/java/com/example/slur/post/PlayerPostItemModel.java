package com.example.slur.post;

import java.io.Serializable;

public class PlayerPostItemModel implements Serializable {

    private int post_id, state;
    private String writer, title;
    private String[] place, need_position;

    public PlayerPostItemModel(int post_id, String writer, String title, int state, String[] place, String[] need_position){
        this.post_id = post_id;
        this.writer = writer;
        this.title = title;
        this.place = place;
        this.state = state;
        this.need_position = need_position;
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
}
