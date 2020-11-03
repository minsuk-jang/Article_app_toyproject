package com.example.myapplication.Parser;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class YtnParser extends BaseParser {
    YtnParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
    }

    @Override
    void init() {
        Document body = Jsoup.parse(text);
/*
        body.select("div.subhead, ab_photo.photo_center, div.ovp_recommend").empty();
        body.select("div.ab_related_article, ab_photo.photo_center, div.ovp_recommend").unwrap();*/

        totalTagNode = cleaner.clean(body.html());

        for (Object obj : totalTagNode.getAllChildren()) {
            addComponent(linearLayout, (TagNode) obj);
        }

        if (!ssb.toString().isEmpty()) {
            TextView textView = makeTextView(0, 3, 0, 4, 10);
            textView.setText(ssb);

            linearLayout.addView(textView);
        }
    }

    @Override
    void addComponent(LinearLayout linearLayout, TagNode tagNode) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                //현재 내용이 존재할 경우,
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent().trim();
                if (!content.isEmpty()) {
                    content = replaceAll(content);

                    int len = content.length();
                    ssb.append(content);
                    String tag_name = tagNode.getName();

                    if (tag_name.equals("b") || tag_name.equals("strong")) {
                        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                        ssb.setSpan(styleSpan, ssb.length() - len, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } else {
                if (obj instanceof TagNode) {
                    TagNode temp = (TagNode) obj;

                    String tag_name = temp.getName();
                    String class_name = temp.getAttributeByName("class");
                    String style_name = temp.getAttributeByName("style");
                    String id_name = temp.getAttributeByName("id");

                    if (tag_name.equals("script") || tag_name.equals("a"))
                        continue;

                    if (style_name != null) {
                        if (style_name.contains("display: none"))
                            continue;
                    }

                    if (tag_name != null) {
                        View view = null;
                        if (tag_name.equals("img")) { //이미지 추가
                            String src = temp.getAttributeByName("src");

                            if (src == null)
                                src = temp.getAttributeByName("data-src");

                            view = makeImageView(src);
                        } else if (tag_name.equals("br") && !ssb.toString().isEmpty()) {//br로 나누기때문에 아래와 같이 진행
                            TextView textView = makeTextView(0, 2, 0, 2, 11);
                            textView.setText(ssb);

                            view = textView;
                            ssb.delete(0, ssb.length());
                        }

                        if (view != null) {
                            linearLayout.addView(view);
                            continue;
                        }
                    }

                    if(id_name != null){
                        View view = null;
                        if(id_name.equals("zumplayer")){
                            String baseURL = "https://pip-player.zum.com/";
                            String type = temp.getAttributeByName("data-type");
                            String invenid = temp.getAttributeByName("data-invenid");
                            String contentid = temp.getAttributeByName("data-contentid");

                            view = makeWebView(0,5,0,10,500,baseURL + type + "?invenid=" + invenid + "&contentid=" + contentid);
                        }

                        if (view != null) {
                            linearLayout.addView(view);
                            continue;
                        }
                    }

                    if (class_name != null) {
                        View view = null;


                        if (view != null) {
                            linearLayout.addView(view);
                            continue;
                        }
                    }

                    addComponent(linearLayout, temp);
                }
            }

        }
    }

    private void adjustAttribute(TagNode tagNode, SpannableStringBuilder spannableStringBuilder) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent().trim();
                String tag_name = tagNode.getName();

                int start = ssb.length();
                int end = start + content.length();

                if (!content.isEmpty()) {
                    spannableStringBuilder.append(content);

                    if (tag_name.equals("b") || tag_name.equals("strong")) {
                        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                        spannableStringBuilder.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (tag_name.matches("[Hh][0-9]+$")) {
                        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(convertSize(tag_name.charAt(1) - '0'));
                        spannableStringBuilder.setSpan(relativeSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } else if (obj instanceof TagNode) {
                adjustAttribute((TagNode) obj, spannableStringBuilder);
            }
        }
    }
}
