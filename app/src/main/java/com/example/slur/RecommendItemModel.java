package com.example.slur;

import java.io.Serializable;

// recommend data model
public class RecommendItemModel implements Serializable {

    private int post_id, user_id;
    private String title, place, play_date, pay, imageStr, university, major, nickname, content, post_date, position_state;
    private String[] need_position;
    private double rating;

    // constructor
    public RecommendItemModel(int post_id, String title, String place, String play_date, String[] need_position, String pay, String imageStr, String university, String major, double rating, String nickname, int user_id, String content, String post_date, String position_state) {
        this.post_id = post_id;
        this.title = title;
        this.place = place;
        this.play_date = play_date;
        this.need_position = need_position;
        this.pay = pay;
        this.imageStr = imageStr;
        this.university = university;
        this.major = major;
        this.rating = rating;
        this.nickname = nickname;
        this.user_id = user_id;
        this.content = content;
        this.post_date = post_date;
        this.position_state = position_state;
    }

    // getters and setters
    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.substring(0, 9);
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

    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public String getPost_date() {
        return post_date;
    }

    public String getPosition_state() {
        return position_state;
    }
}
