package com.example.myapplication.Parser;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
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

        body.select("div.ab_related_article, ab_photo.photo_center, div.ovp_recommend, div.ab_box_article.ab_division.division_left, div.ab_box_article.ab_division.division_right").empty();
        body.select("div.ab_related_article, ab_photo.photo_center, div.ovp_recommend, div.ab_box_article.ab_division.division_left, div.ab_box_article.ab_division.division_right").unwrap();
        totalTagNode = cleaner.clean(body.html());

        for (Object obj : totalTagNode.getAllChildren()) {
            addComponent(linearLayout, (TagNode) obj);
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
                            TextView textView = makeTextView(0, 3, 0, 7, text_size);
                            textView.setText(ssb);

                            view = textView;
                            ssb.delete(0, ssb.length());
                        }

                        if (view != null) {
                            linearLayout.addView(view);
                            continue;
                        }
                    }

                    if (class_name != null) {
                        View view = null;
                        if (class_name.equals("tag_vod")) { //영상에 관련된 뷰
                            String data_id = temp.getAttributeByName("data-id");
                            String data_service = temp.getAttributeByName("data-service");

                            if (data_service.equals("ovp")) {
                                int idx = data_id.indexOf("?");
                                data_id = data_id.substring(0,idx);
                                data_id = "https://oya.joins.com/bc_iframe.html?videoId=" + data_id;
                            }
                            //todo 높이 재조정 필요
                            view = makeWebView(0, 5, 0, 10, 800, data_id);
                        } else { //그 외 나머지 처리
                            SpannableStringBuilder ssb = new SpannableStringBuilder();
                            if (class_name.equals("caption")) {
                                TextView textView = makeTextView(0, 0, 0, 3, caption_size);
                                textView.setTextColor(Color.parseColor("#737475"));

                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);
                                view = textView;
                            } else if (class_name.equals("ab_subtitle")) {
                                TextView textView = makeTextView(0, 20, 0, 20, 15);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextColor(Color.parseColor("#3c3e40"));
                                textView.setPadding(20, 0, 0, 0);
                                textView.setBackground(getDrawable(R.drawable.joongang_ab_subtitle));

                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);
                                view = textView;
                            } else if (class_name.equals("ab_sub_heading")) {
                                TextView textView = makeTextView(0, 50, 15, 15, 30);
                                textView.setPadding(0, 20, 0, 20);
                                textView.setBackground(getDrawable(R.drawable.joongang_ab_sub_heading));

                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);
                                view = textView;
                            } else if (class_name.equals("ab_box_article")) {
                                LinearLayout temp_layout = makeLinearLayout(4, 30, 4, 30, LinearLayout.VERTICAL);
                                temp_layout.setBackground(getDrawable(R.drawable.dongatableborder));
                                temp_layout.setPadding(30, 20, 30, 20);

                                addComponent(temp_layout, temp);
                                linearLayout.addView(temp_layout);
                                continue;
                            } else if (class_name.equals("ab_box_titleline")) {
                                TextView textView = makeTextView(0, 10, 0, 10, headline_size);
                                textView.setTextColor(Color.parseColor("#5d81c3"));
                                adjustAttribute(temp, ssb);

                                textView.setText(ssb);
                                view = textView;
                            } else if (class_name.equals("ab_quotation")) {
                                LinearLayout temp_layout = makeLinearLayout(0, 10, 0, 10, LinearLayout.HORIZONTAL);
                                temp_layout.setGravity(Gravity.CENTER_VERTICAL);
                                ImageView quote = new ImageView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 0, 5, 0);
                                quote.setLayoutParams(params);
                                quote.setImageResource(R.drawable.quote_black);

                                TextView textView = makeTextView(20, 0, 0, 0, 15);
                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);

                                linearLayout.addView(quote);
                                linearLayout.addView(textView);

                                this.linearLayout.addView(temp_layout);
                            }
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
                String tag_name = ((TagNode) obj).getName();

                if (tag_name.equals("br"))
                    spannableStringBuilder.append("\n");
                else
                    adjustAttribute((TagNode) obj, spannableStringBuilder);
            }
        }
    }


}
