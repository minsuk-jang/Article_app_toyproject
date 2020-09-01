package com.example.myapplication.DataVO;


import org.jsoup.select.Elements;

public class ArticleVO {
    String subject, img_src, title, content;

    public ArticleVO(String subject, String img_src, String title, String content) {
        this.subject = subject;
        this.content = content;
        this.title = title;
        this.img_src = img_src;

    }

    public String getSubject() {
        return subject;
    }

    public String getImg_src() {
        return img_src;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
