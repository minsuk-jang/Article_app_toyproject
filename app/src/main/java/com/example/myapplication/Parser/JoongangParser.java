package com.example.myapplication.Parser;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.widget.TextViewCompat;

import com.example.myapplication.R;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JoongangParser extends BaseParser {
    private boolean table_first = false;

    public JoongangParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
    }

    @Override
    void init() {
        Document body = Jsoup.parse(text);

        // body.select("div.ab_related_article, ab_photo.photo_center, div.ovp_recommend").empty();
        // body.select("div.ab_related_article, ab_photo.photo_center, div.ovp_recommend").unwrap()
        totalTagNode = cleaner.clean(body.html());
        addComponent(totalTagNode);

    }

    @Override
    void addComponent(TagNode tagNode) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                //현재 내용이 존재할 경우,
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent().trim();

                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    addTextTagComponent(content);
                }

            } else {
                if (obj instanceof TagNode) {
                    TagNode temp = (TagNode) obj;

                    String tag_name = temp.getName();
                    String class_name = temp.getAttributeByName("class");

                    if (tag_name.equals("script") || tag_name.equals("a"))
                        continue;

                    if (tag_name != null && tag_name.equals("img")) {
                        String src = temp.getAttributeByName("src");

                        if (src == null)
                            src = temp.getAttributeByName("data-src");

                        linearLayout.addView(makeImageView(src));
                    } else if (class_name != null && class_name.equals("ab_box_article")) {
                        LinearLayout linearLayout = makeLinearLayout(4, 30, 4, 30, LinearLayout.VERTICAL);
                        linearLayout.setBackground(getDrawable(R.drawable.dongatableborder));
                        linearLayout.setPadding(30, 0, 30, 0);

                        makeBox(linearLayout, temp);

                        this.linearLayout.addView(linearLayout);
                    } else if (class_name != null && class_name.equals("tag_vod")) {
                        String data_id = temp.getAttributeByName("data-id");

                        //todo WebView 높이 사이즈 재조정 필요요
                        linearLayout.addView(makeWebView(0, 5, 0, 10, 800, makeUrl(data_id)));

                    } else if (class_name != null && class_name.equals("ab_sub_heading")) {
                        TextView textView = makeTextView(0, 50, 0, 15, 30);
                        textView.setPadding(0, 20, 0, 20);
                        textView.setBackground(getDrawable(R.drawable.joongang_ab_sub_heading));
                        textView.setTextColor(Color.parseColor("#000000"));
                        giveAttribute(temp, textView);

                        linearLayout.addView(textView);
                    } else if (class_name != null && class_name.equals("ab_subtitle")) {
                        TextView textView = makeTextView(0, 20, 0, 20, 13);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setTextColor(Color.parseColor("#3C3E40"));
                        textView.setPadding(20, 0, 0, 0);
                        textView.setBackground(getDrawable(R.drawable.joongang_ab_subtitle));
                        giveAttribute(temp, textView);

                        linearLayout.addView(textView);
                    } else if (class_name != null && class_name.equals("ab_quotation")) {
                        LinearLayout linearLayout = makeLinearLayout(0, 10, 0, 10, LinearLayout.HORIZONTAL);
                        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                        ImageView quote = new ImageView(context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 5, 0);
                        quote.setLayoutParams(params);
                        quote.setImageResource(R.drawable.quote_black);

                        TextView textView = makeTextView(20, 0, 0, 0, 15);
                        textView.setTextColor(Color.parseColor("#000000"));
                        giveAttribute(temp, textView);

                        linearLayout.addView(quote);
                        linearLayout.addView(textView);

                        this.linearLayout.addView(linearLayout);
                    } else if (class_name != null && class_name.equals("caption")) {
                        TextView textView = makeTextView(0, 0, 0, 3, 10);
                        textView.setTextColor(Color.parseColor("#737475"));
                        giveAttribute(temp, textView);

                        linearLayout.addView(textView);
                    } else
                        addComponent(temp);
                }
            }

        }
    }

    private void makeBox(LinearLayout linearLayout, TagNode tagNode) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent().trim();

                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    TextView textView = makeTextView(0, 5, 0, 5, 10);
                    textView.setText(content);
                    linearLayout.addView(textView);
                }
            } else {
                if (obj instanceof TagNode) {
                    TagNode tagNode1 = (TagNode) obj;
                    String class_name = tagNode1.getAttributeByName("class");
                    String tag_name = tagNode1.getName();


                    if (tag_name != null && tag_name.equals("img")) {
                        String src = tagNode1.getAttributeByName("src");
                        if (src == null)
                            src = tagNode1.getAttributeByName("data-src");
                        linearLayout.addView(makeImageView(src));
                    } else if (class_name != null && class_name.equals("ab_box_title")) {
                        TextView textView = makeTextView(0, 10, 0, 10, 13);
                        textView.setTextColor(Color.parseColor("#5d91c3"));
                        textView.setTypeface(null, Typeface.BOLD);
                        giveAttribute(tagNode1, textView);

                        linearLayout.addView(textView);
                    } else if (class_name != null && class_name.equals("ab_box_content")) {
                        TextView textView = makeTextView(0, 5, 0, 5, 10);
                        giveAttribute(tagNode1, textView);

                        linearLayout.addView(textView);
                    }else
                        makeBox(linearLayout,tagNode1);
                }
            }
        }

        return;
    }

    private void addTextTagComponent(String content) {
        TextView textView = makeTextView(0, 10, 0, 10, 13);
        textView.setTextColor(Color.parseColor("#000000"));
        TextViewCompat.setTextAppearance(textView,
                android.R.style.TextAppearance_Material_Body1);
        textView.setText(content);

        linearLayout.addView(textView);
    }


    private void giveAttribute(TagNode tagNode, TextView textView) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                ContentNode contentNode = (ContentNode) obj;

                String content = contentNode.getContent().trim();

                if(tagNode.getName().equals("br")){
                    content = replaceAll(content);
                    String temp = textView.getText().toString();

                    if (temp != null)
                        textView.setText(temp + content);
                    else
                        textView.setText(content);
                }

                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    String temp = textView.getText().toString();

                    //이전에 내용이 존재할 경우
                    if (temp != null)
                        textView.setText(temp + content);
                    else
                        textView.setText(content);
                }

            } else {
                if (obj instanceof TagNode) {
                    TagNode temp = (TagNode) obj;
                    String tag = temp.getName();

                    if (tag.equals("font")) {
                        String color = temp.getAttributeByName("color");
                        textView.setTextColor(Color.parseColor(color));
                    }

                    if (tag.matches("[Hh][0-9]+$")) {
                        textResize(textView, tag.charAt(1) - '0');
                    }

                    if (tag.equals("p")) {
                        textView.setTypeface(null, Typeface.BOLD);
                    }

                    giveAttribute(temp, textView);
                }
            }
        }

        return;
    }

}
