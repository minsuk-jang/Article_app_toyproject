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

public class KukminParser extends BaseParser {

    public KukminParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
    }

    @Override
    void init() {
        Document body = Jsoup.parse(text);

        body.select("strong").empty();
        body.select("strong").unwrap();

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

                    if (tag_name.equals("b")) {
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
                        }else {
                            SpannableStringBuilder ssb = new SpannableStringBuilder();

                            if (tag_name.equals("figcaption")) {
                                TextView textView = makeTextView(0, 0, 0, 3, 8);
                                textView.setTextColor(Color.parseColor("#737475"));

                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);
                                view = textView;
                            }
                        }

                        if (view != null) {
                            linearLayout.addView(view);
                            continue;
                        }
                    }

                    if(style_name != null){
                        View view = null;
                        SpannableStringBuilder ssb = new SpannableStringBuilder();

                        if(style_name.contains("padding-top:15px;padding-bottom:15px;border-top:1px solid #444;border-bottom:1px solid #eee;color:#333;font-size:20px;line-height:1.4;font-weight: bold;letter-spacing: -0.0733em;")) {
                            TextView textView = makeTextView(0, 50, 15, 15, 14);
                            textView.setPadding(0, 20, 0, 20);
                            textView.setBackground(getDrawable(R.drawable.kukmin_ab_subtitle));

                            adjustAttribute(temp, ssb);
                            textView.setText(ssb);
                            view = textView;
                        }

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
