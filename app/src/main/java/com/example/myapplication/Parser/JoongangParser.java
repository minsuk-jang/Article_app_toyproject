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

    }

    @Override
    void addComponent() {

    }
}
