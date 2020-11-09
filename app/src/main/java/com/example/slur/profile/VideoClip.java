package com.example.slur.profile;

import android.widget.ImageView;

 //clip 데이터 클래스
public class VideoClip {
    String url;
    ImageView thumbnail;
    String title;

    public VideoClip(String url, ImageView thumbnail, String title) {
        this.url = url;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageView getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}