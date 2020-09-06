package com.example.myapplication.Parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.apache.tools.ant.taskdefs.EchoXML;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.provider.CalendarContract.CalendarCache.URI;

public class DongaParser {
    private String text;
    private LinearLayout linearLayout;
    private HtmlSpanner spanner;

    public DongaParser(LinearLayout linearLayout, String text) {
        this.text = text;
        this.linearLayout = linearLayout;
        this.spanner = new HtmlSpanner();
        init();
    }

    private void init() {
        Document body = Jsoup.parse(text);

        body.select("div.reporter_view, div.txt_ban, div.article_relation, ul.relation_list, div#bestnews_layer").empty();
        body.select("div.reporter_view, div.txt_ban, div.article_relation, ul.relation_list, div#bestnews_layer").unwrap();
        body.select("span.thumb").unwrap();

        text = Jsoup.clean(body.outerHtml(), Whitelist.basic().addAttributes("p", "class")
                .addAttributes("span", "class")
                .addAttributes("img", "data-src", "src")
                .addAttributes("strong", "class")
                .addAttributes("div", "class"));

        spanner.registerHandler("span", new SpanTagHandler());
        spanner.registerHandler("img", new ImageTagHandler());
        spanner.registerHandler("strong", new StrongTagHandler());
        spanner.registerHandler("div", new DivTagHandler());

    }

    private class DivTagHandler extends TagNodeHandler {
        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
            String class_name = node.getAttributeByName("class");

            if (class_name != null && class_name.equals("wpsArticleHtmlComponent")) {
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.2F);
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3e508d"));
                CustomSpan customSpan = new CustomSpan();

                builder.setSpan(customSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.insert(start,"\n");
                builder.insert(end+1,"\n\n\n");
            }
        }
    }

    //위와 아래의 선을 그리기 위한 Span
    private class CustomSpan extends DynamicDrawableSpan {
        @Override
        public Drawable getDrawable() {
            return null;
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            return (int) paint.measureText(text, start, end);
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            int len = (int) paint.measureText(text, start, end);

            paint.setColor(Color.parseColor("#cfcfcf"));
            paint.setStrokeWidth(2);
            canvas.drawLine(x, bottom + 20, len + x, bottom + 20, paint);

            paint.setColor(Color.parseColor("#3e508d"));
            paint.setStrokeWidth(2);
            canvas.drawLine(x, top - 30 , len + x, top - 30, paint);
            canvas.drawText(text, start, end, x, y, paint);
        }
    }

    private class SpanTagHandler extends TagNodeHandler {
        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
            String class_name = node.getAttributeByName("class");

            if (class_name.equals("txt")) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#828282"));
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
                AlignmentSpan.Standard alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);

                builder.setSpan(alignmentSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.insert(end,"\n");

            }
        }
    }

    private class StrongTagHandler extends TagNodeHandler {
        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
            String class_name = node.getAttributeByName("class");

            if (class_name.equals("sub_title") || class_name.equals("article_view")) {
                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.2F);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#3e508d"));
                LeadingMarginSpan.Standard leadingMarginSpan = new LeadingMarginSpan.Standard(20);
                QuoteSpan quote = new QuoteSpan(Color.parseColor("#3e508d"));

                builder.setSpan(quote, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(leadingMarginSpan, start, end, 0);
                builder.setSpan(relativeSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.insert(end,"\n");

            }
        }
    }

    private class ImageTagHandler extends TagNodeHandler {
        @Override
        public void handleTagNode(TagNode node, final SpannableStringBuilder builder, final int start, final int end) {
            String src = node.getAttributes().get("src");

            builder.append("\n\ufffc\n\n");
            try {
                Drawable dr = new AsyncTask<String, Void, Drawable>() {
                    @Override
                    protected Drawable doInBackground(String... strings) {
                        try {
                            Drawable dr = Glide.with(linearLayout.getContext())
                                    .load(strings[0])
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .submit().get();

                            return dr;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(src).get();

                dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

                ImageSpan span = new ImageSpan(dr);
                builder.setSpan(span, start + 1, end + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            AlignmentSpan.Standard alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
            builder.setSpan(alignmentSpan, start + 1, end + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public Spanned parsing() {
        return spanner.fromHtml(text);
    }

}
