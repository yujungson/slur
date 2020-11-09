package com.example.slur.post;

import java.io.Serializable;

public class positionItem implements Serializable {

    private String position;
    private int state;

    public positionItem(String position, String state) {
        this.position = position;
        this.state = Integer.parseInt(state);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
