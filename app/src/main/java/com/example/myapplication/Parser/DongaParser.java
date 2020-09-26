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
import android.text.style.UnderlineSpan;
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
    private boolean table_first = false;

    public DongaParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
    }

    void init() {
        Document body = Jsoup.parse(text);
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword, div.kims_news").empty();
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword, div.kims_news").unwrap();

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
                    } else if (tag_name != null && tag_name.equals("table")) {
                        LinearLayout linearLayout = makeLinearLayout(4, 30, 4, 30, LinearLayout.VERTICAL);
                        linearLayout.setBackground(getDrawable(R.drawable.dongatableborder));
                        linearLayout.setPadding(30, 0, 30, 0);

                        makeTable(linearLayout, tagNode);

                        this.linearLayout.addView(linearLayout);
                    } else if (tag_name != null && tag_name.equals("b")) {
                        TextView textView = makeTextView(0, 15, 0, 15, 15);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setTextColor(Color.parseColor("#000000"));
                        giveAttribute(temp, textView);

                        if (!textView.getText().toString().isEmpty())
                            linearLayout.addView(textView);
                    } else if (class_name != null && class_name.equals("sub_title")) {
                        TextView textView = makeTextView(0, 30, 0, 30, 17);
                        textView.setBackground(getDrawable(R.drawable.donga_textborder));
                        textView.setPadding(20, 0, 0, 0);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setTextColor(Color.parseColor("#3e508d"));
                        giveAttribute(temp, textView);

                        linearLayout.addView(textView);
                    } else if (class_name != null && class_name.equals("wpsArticleHtmlComponent")) {
                        TextView textView = makeTextView(0, 10, 0, 10, 15);
                        textView.setBackground(getDrawable(R.drawable.donga_wpsarticlehtmlcomponent));
                        textView.setPadding(0, 30, 0, 30);
                        textView.setTextColor(Color.parseColor("#3e508d"));
                        giveAttribute(temp, textView);

                        linearLayout.addView(textView);
                    } else if (class_name != null && class_name.equals("txt")) {
                        TextView textView = makeTextView(0, 0, 0, 3, 10);
                        textView.setTextColor(Color.parseColor("#828282"));
                        giveAttribute(temp, textView);

                        linearLayout.addView(textView);
                    }else
                        addComponent(temp);
                }
            }

        }
    }

    private void makeTable(LinearLayout linearLayout, TagNode tagNode) {
        for (Object obj : tagNode.getAllChildren()) {
            if (obj instanceof ContentNode) {
                ContentNode contentNode = (ContentNode) obj;
                String content = contentNode.getContent().trim();

                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    TextView textView = makeTextView(0, 10, 0, 10, 12);
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

                if (!content.isEmpty()) {
                    content = replaceAll(content);
                    textView.setText(content);
                }

            } else {
                if (obj instanceof TagNode) {
                    TagNode temp = (TagNode) obj;
                    String tag = temp.getName();

                    if (tag.equals("a"))
                        continue;

                    if (tag.equals("font")) {
                        String color = temp.getAttributeByName("color");
                        textView.setTextColor(Color.parseColor(color));
                    }

                    giveAttribute(temp, textView);
                }
            }
        }

        return;
    }

}
