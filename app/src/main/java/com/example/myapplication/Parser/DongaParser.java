package com.example.myapplication.Parser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.util.Map;

public class DongaParser extends BaseParser {

    public DongaParser(LinearLayout linearLayout, String text) {
        super(linearLayout, text);
        init();
    }

    void init() {
        Document body = Jsoup.parse(text);

        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword").empty();
        body.select("div.reporter_view, div.txt_ban, div.bestnews, div.article_relation, ul.relation_list, div#bestnews_layer, div.article_keyword").unwrap();
        body.select("span.thumb").unwrap();
        text = Jsoup.clean(body.outerHtml(), Whitelist.basic().addAttributes("p", "class")
                .addAttributes("span", "class")
                .addAttributes("img", "data-src", "src")
                .addAttributes("strong", "class")
                .addAttributes("div", "class"));

        HtmlParser parser = new HtmlParser(linearLayout, text);
        lists = parser.preprocessing();
    }


    @Override
    void addComponent() {
        for (Map<String, String> map : lists) {
            String tag_name = map.get("tag");

            if (tag_name.equals("img")) {
                String src = map.get("src");

                if (src == null)
                    src = map.get("data-src");

                addImageViewComponent(src);
            } else if (tag_name.equals("strong")) {
                String class_name = map.get("class");

                if (class_name.equals("sub_title")) {
                    TextView textView = makeTextViewComponent(0, 0, 3, 3);
                    textView.setText(map.get("content"));
                    textView.setTextColor(Color.parseColor("#3e508d"));
                    textView.setPadding(15, 0, 0, 0);
                    textView.setTextSize(17);
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setBackground(ResourcesCompat.getDrawable(linearLayout.getResources(), R.drawable.donga_textborder, null));

                    linearLayout.addView(textView);
                }
            } else if (tag_name.equals("span")) {
                String class_name = map.get("class");

                if (class_name != null && class_name.equals("txt")) {
                    TextView textView = makeTextViewComponent(0, 0, 0, 3);
                    textView.setText(map.get("content"));
                    textView.setTextColor(Color.parseColor("#828282"));
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextSize(10);

                    linearLayout.addView(textView);
                }
            } else if (tag_name.equals("div")) {
                String class_name = map.get("class");

                if (class_name != null) {
                    if (class_name.equals("wpsArticleHtmlComponent")) {
                        TextView textView = makeTextViewComponent(0, 0, 5, 5);
                        textView.setText(map.get("content"));
                        textView.setTextColor(Color.parseColor("#3e508d"));
                        textView.setBackground(ResourcesCompat.getDrawable(linearLayout.getResources(),R.drawable.donga_wpsarticlehtmlcomponent,null));
                        textView.setTextSize(17);

                        linearLayout.addView(textView);
                    }
                }else{
                    String content = map.get("content");

                    if(!content.trim().isEmpty()) {
                        TextView textView = makeTextViewComponent(0, 0, 3, 3);
                        textView.setText(map.get("content"));
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setTextSize(15);
                        TextViewCompat.setTextAppearance(textView, android.R.style.TextAppearance_Material_Body1);

                        linearLayout.addView(textView);
                    }
                }
            } else {
                String content = map.get("content");

                if (!content.trim().isEmpty()) {
                    TextView textView = makeTextViewComponent(0, 0, 3, 3);
                    textView.setText(content);
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setTextSize(15);
                    TextViewCompat.setTextAppearance(textView, android.R.style.TextAppearance_Material_Body1);

                    linearLayout.addView(textView);
                }
            }
        }
    }
}
