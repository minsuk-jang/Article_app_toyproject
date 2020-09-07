package com.example.myapplication.Parser;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

public class JoongangParser extends BaseParser {
    public JoongangParser(LinearLayout linearLayout, String text) {
        super(linearLayout,text);
        init();
    }

    @Override
    void init() {

        Document body = Jsoup.parse(text);

        body.select("div.ab_related_article, ab_photo.photo_center").empty();
        body.select("div.ab_related_article, ab_photo.photo_center").unwrap();

        text = body.html();

        spanner.registerHandler("img",new ImageTagHandler());
        spanner.registerHandler("p",new PTagHandler());
        spanner.registerHandler("div",new DivTagHandler());
    }


    private class PTagHandler extends TagNodeHandler{
        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
            String class_name = node.getAttributeByName("class");

            if(class_name != null && class_name.equals("caption")){
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#828282"));
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
                AlignmentSpan.Standard alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);

                builder.setSpan(alignmentSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.insert(end, "\n\n");
            }else {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(styleSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private class DivTagHandler extends TagNodeHandler{
        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end) {
            String class_name = node.getAttributeByName("class");

            if(class_name.equals("ab_sub_heading")){
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.2F);
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3e508d"));
                CustomSpan customSpan = new CustomSpan();

                builder.setSpan(customSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.insert(start, "\n\n");
                builder.insert(end + 2, "\n\n");
            }else if(class_name.equals("ab_subtitle")){
                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.9F);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                LeadingMarginSpan.Standard leadingMarginSpan = new LeadingMarginSpan.Standard(20);
                QuoteSpan quote = new QuoteSpan(Color.parseColor("#000000"));

                builder.setSpan(quote, start, end-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(leadingMarginSpan, start, end-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(relativeSizeSpan, start, end-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(styleSpan, start, end-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }else if(class_name.equals("ab_quotation")){
                if(isIcon(node)) {
                    Drawable dr = getDrawable("https://images.joins.com/ui_joongang/news/pc/article/i_quote.png");
                    IconMarginSpan iconMarginSpan = new IconMarginSpan(((BitmapDrawable) dr).getBitmap(), 6);

                    builder.setSpan(iconMarginSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }else if(class_name.equals("ab_box_article")){
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#5d81c3"));

                builder.setSpan(styleSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(foregroundColorSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private boolean isIcon(TagNode current){
        for(Object child : current.getChildren()){
            if(child instanceof TagNode) {
                TagNode tagNode = (TagNode)child;
                String class_name = tagNode.getAttributeByName("class");

                if(class_name != null && class_name.equals("icon"))
                    return true;
                else
                    return isIcon(tagNode);
            }
        }

        return false;
    }

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

            paint.setColor(Color.parseColor("#ebebeb"));
            paint.setStrokeWidth(2);
            canvas.drawLine(x, bottom + 45, len + x, bottom + 45, paint);

            paint.setColor(Color.parseColor("#444446"));
            paint.setStrokeWidth(2);
            canvas.drawLine(x, top - 45, len + x, top - 45, paint);
            canvas.drawText(text, start, end, x, y, paint);
        }
    }

}
