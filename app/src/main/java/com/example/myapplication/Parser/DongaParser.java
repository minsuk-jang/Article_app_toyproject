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
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
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

    public DongaParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
    }

    void init() {
        Document body = Jsoup.parse(text);
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword").empty();
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword").unwrap();

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
                String tag_name = tagNode.getName();
                String class_name = tagNode.getAttributeByName("class");

                if (tag_name.equals("span")) {
                    addSpanTagComponent(content, class_name);
                } else if (tag_name.equals("div")) {
                    addDivTagComponent(content, class_name);
                } else if (tag_name.equals("strong")) {
                    addStrongTagComponent(content, class_name);
                } else if (!content.isEmpty()) {
                    addTextTagComponent(content);
                }

            } else {
                if (obj instanceof TagNode) {
                    TagNode temp = (TagNode) obj;
                    String tag_name = temp.getName();
                    if (tag_name.equals("img"))
                        addImageTagComponent(temp.getAttributeByName("src"));
                    else if (tag_name.equals("table")) {
                        addTableComponent(temp);
                    } else if (!tag_name.equals("script"))
                        addComponent(temp);
                }
            }

        }
    }

    private void addTableComponent(TagNode tagNode) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 5, 4, 5);

        TextView textView = new TextView(linearLayout.getContext());
        textView.setLayoutParams(params);
        TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Body1);
        textView.setTextSize(12);
        textView.setBackground(ResourcesCompat.getDrawable(linearLayout.getResources(), R.drawable.dongatableborder, null));
        textView.setPadding(5, 0, 5, 0);

        StringBuilder sb = new StringBuilder();
        searchTable(sb, tagNode);

        textView.setText(sb.toString());
        linearLayout.addView(textView);
    }

    private void searchTable(StringBuilder sb, TagNode tagNode) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent();

                sb.append(content);
            } else {
                if (obj instanceof TagNode) {
                    TagNode tagNode1 = (TagNode) obj;
                    if (tagNode1.getName().equals("br"))
                        sb.append("\n");
                    else
                        searchTable(sb, tagNode1);
                }
            }
        }

        return;
    }


    private void addTextTagComponent(String content) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 3, 0, 3);

        TextView textView = new TextView(linearLayout.getContext());
        textView.setLayoutParams(params);
        textView.setTextColor(Color.parseColor("#000000"));
        TextViewCompat.setTextAppearance(textView,
                android.R.style.TextAppearance_Material_Body1);
        textView.setTextSize(13);
        textView.setText(content);

        linearLayout.addView(textView);
    }

    private void addDivTagComponent(String content, String class_name) {
        if (class_name != null) {
            if (class_name.equals("wpsArticleHtmlComponent")) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150);
                params.setMargins(0, 30, 0, 30);

                TextView textView = new TextView(linearLayout.getContext());
                textView.setLayoutParams(params);
                textView.setTextColor(Color.parseColor("#3e508d"));
                textView.setBackground(ResourcesCompat.getDrawable(linearLayout.getResources(), R.drawable.donga_wpsarticlehtmlcomponent, null));
                textView.setText(content);
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER_VERTICAL);

                linearLayout.addView(textView);
            }
        } else {
            if (!content.isEmpty()) {
                addTextTagComponent(content);
            }
        }
    }

    private void addSpanTagComponent(String content, String class_name) {
        if (class_name.equals("txt")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 1, 0, 5);
            TextView textView = new TextView(linearLayout.getContext());
            textView.setLayoutParams(params);
            textView.setTextColor(Color.parseColor("#828282"));
            textView.setTextSize(10);
            textView.setText(content);

            linearLayout.addView(textView);
        }
    }

    private void addStrongTagComponent(String content, String class_name) {
        if (class_name != null) {
            if (class_name.equals("sub_title")) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);

                TextView textView = new TextView(linearLayout.getContext());
                textView.setLayoutParams(params);
                textView.setTextColor(Color.parseColor("#3e508d"));
                textView.setTextSize(18);
                textView.setText(content);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(15, 0, 0, 0);
                textView.setBackground(ResourcesCompat.getDrawable(linearLayout.getResources(), R.drawable.donga_textborder, null));

                linearLayout.addView(textView);
            }
        }
        //todo 개선

    }

    private void addImageTagComponent(String src) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 15, 0, 10);

        ImageView imageView = new ImageView(linearLayout.getContext());
        Glide.with(linearLayout.getContext())
                .load(src)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);

        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(params);

        linearLayout.addView(imageView);
    }
}
