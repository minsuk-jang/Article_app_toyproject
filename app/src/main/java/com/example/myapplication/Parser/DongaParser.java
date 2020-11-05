package com.example.myapplication.Parser;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DongaParser extends BaseParser {
    private boolean table_first = false;

    public DongaParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
    }

    void init() {
        Document body = Jsoup.parse(text);
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword, div.kims_news").empty();
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword, div.kims_news").unwrap();

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
                int start = ssb.length();
                int end = start + content.length();
                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    ssb.append(content);
                    String tag_name = tagNode.getName();

                    if (tag_name.equals("b") || tag_name.equals("strong")) {
                        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                        ssb.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } else {
                if (obj instanceof TagNode) {
                    TagNode temp = (TagNode) obj;

                    String tag_name = temp.getName();
                    String class_name = temp.getAttributeByName("class");

                    if (tag_name.equals("script") || tag_name.equals("a"))
                        continue;

                    if (tag_name != null) {
                        View view = null;
                        if (tag_name.equals("img")) { //이미지 추가
                            String src = temp.getAttributeByName("src");

                            if (src == null)
                                src = temp.getAttributeByName("data-src");

                            view = makeImageView(src);
                        } else if (tag_name.equals("br") && !ssb.toString().isEmpty()) {//br로 나누기때문에 아래와 같이 진행
                            TextView textView = makeTextView(0, 3, 0, 8, text_size);
                            textView.setText(ssb);

                            view = textView;
                            ssb.delete(0, ssb.length());
                        } else if (tag_name.equals("table")) {
                            LinearLayout temp_layout = makeLinearLayout(5, 10, 5, 10, LinearLayout.VERTICAL);
                            temp_layout.setBackground(getDrawable(R.drawable.dongatableborder));
                            temp_layout.setPadding(25, 5, 25, 5);

                            makeTable(temp_layout, temp);
                            linearLayout.addView(temp_layout);
                            continue;
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

                            //todo 높이 재조정 필요
                            view = makeWebView(0, 5, 0, 10, 800, makeUrl(data_id));
                        } else { //그 외 나머지 처리
                            SpannableStringBuilder ssb = new SpannableStringBuilder();
                            if (class_name.equals("txt")) {
                                TextView textView = makeTextView(0, 0, 0, 3, caption_size);
                                textView.setTextColor(Color.parseColor("#828282"));

                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);
                                view = textView;
                            } else if (class_name.equals("sub_title")) {
                                TextView textView = makeTextView(0, 30, 0, 30, headline_size);
                                textView.setBackground(getDrawable(R.drawable.donga_textborder));
                                textView.setPadding(20, 0, 0, 0);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextColor(Color.parseColor("#3e508d"));

                                adjustAttribute(temp, ssb);
                                textView.setText(ssb);
                                view = textView;
                            } else if (class_name.equals("wpsArticleHtmlComponent")) {
                                TextView textView = makeTextView(0, 10, 0, 10, 15);
                                textView.setBackground(getDrawable(R.drawable.donga_wpsarticlehtmlcomponent));
                                textView.setPadding(0, 30, 0, 30);
                                textView.setTextColor(Color.parseColor("#3e508d"));

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

                    addComponent(linearLayout, temp);
                }
            }

        }
    }

    //todo 걔선 필
    private void makeTable(LinearLayout linearLayout, TagNode tagNode) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent().trim();

                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    TextView textView = makeTextView(0, 10, 0, 10, text_size);
                    TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Body1);
                    textView.setText(content);

                    if (!table_first) {
                        table_first = true;
                        textView.setBackground(getDrawable(R.drawable.dongatablefirsttext));
                        textView.setTextColor(Color.parseColor("#3d65de"));
                        textView.setPadding(0, 30, 0, 15);
                    }

                    linearLayout.addView(textView);
                }
            } else {
                if (obj instanceof TagNode) {
                    TagNode tagNode1 = (TagNode) obj;
                    makeTable(linearLayout, tagNode1);
                }
            }
        }

        return;
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
                TagNode temp = (TagNode) obj;

                if (temp.getName().equals("br"))
                    spannableStringBuilder.append("\n");
                else
                    adjustAttribute((TagNode) obj, spannableStringBuilder);
            }
        }
    }

}
