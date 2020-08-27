package com.example.myapplication.DataVO;


public class ArticleVO {
    String img_url, title, content;

    public ArticleVO(String img_url, String title, String content) {
        this.content = content;
        this.img_url = img_url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImg_url() {
        return img_url;
    }

}
