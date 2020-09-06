package com.example.myapplication.Parser;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class JoongangParser {
    private String text;
    private LinearLayout linearLayout;

    public JoongangParser(LinearLayout linearLayout, String text) {
        this.text = text;
        this.linearLayout = linearLayout;

    }

    public String parsing() {
        Document body = Jsoup.parse(text);

        body.select("div.ab_related_article").empty();
        body.select("div.ab_related_article").unwrap();
/*
        text = Jsoup.clean(body.html(), Whitelist.basicWithImages().addAttributes("p", "class")
                .addTags("strong")
                .addAttributes("img", "data-src", "src")
                .addAttributes("strong", "class")
                .addTags("h/[^0-9]/g")
        );*/

        body = Jsoup.parse(text);
        body.select("a[rel=\"nofollow\"]").empty();
        body.select("a[rel=\"nofollow\"]").unwrap();

        String head =

                text = body.html();

        return text;
    }

}
