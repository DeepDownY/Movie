package com.example.y1247.movie.data;

/**
 * Created by y1247 on 2017/5/1.
 */

public class Review {
    String id;
    String author;
    String content;
    String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
