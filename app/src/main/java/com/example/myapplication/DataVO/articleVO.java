package com.example.myapplication.DataVO;

public class articleVO {
    String img_url, url, title, content;

    public articleVO(String img_url, String url, String title, String content) {
        this.content = content;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
