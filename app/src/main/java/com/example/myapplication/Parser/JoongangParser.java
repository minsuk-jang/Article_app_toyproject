package com.example.myapplication.Parser;

import android.widget.LinearLayout;

import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JoongangParser extends BaseParser {
    public JoongangParser(LinearLayout linearLayout, String text) {
        super(linearLayout,text);
        init();
    }

    @Override
    void init() {

        Document body = Jsoup.parse(text);

        body.select("div.ab_related_article, ab_photo.photo_center").empty();
        body.select("div.ab_related_article, ab_photo.photo_center").unwrap();

        text = body.html();

    }

    @Override
    void addComponent(TagNode tagNode) {

    }
}
