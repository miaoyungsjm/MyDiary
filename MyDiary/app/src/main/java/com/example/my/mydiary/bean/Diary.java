package com.example.my.mydiary.bean;

public class Diary {
    private Integer id;
    private String title;
    private String content;
    private String pubDate;

    public Diary(Integer id, String title, String content, String pubDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pubDate = pubDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
