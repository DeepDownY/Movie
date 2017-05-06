package com.example.y1247.movie.data;

/**
 * Created by y1247 on 2017/5/1.
 */

public class Video {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String name;
    String key;

    public Video(String name, String key) {
        this.name = name;
        this.key = key;
    }



}
