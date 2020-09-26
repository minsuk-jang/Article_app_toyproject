package com.example.myapplication.Parser;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public abstract class BaseParser {
    protected LinearLayout linearLayout;
    protected String text;
    protected TagNode totalTagNode = null;
    protected HtmlCleaner cleaner;
    protected Context context;
    protected SpannableStringBuilder ssb;

    protected BaseParser(LinearLayout linearLayout, String text) {
        this.linearLayout = linearLayout;
        this.text = text;
        this.context = linearLayout.getContext();
        cleaner = new HtmlCleaner();
        ssb = new SpannableStringBuilder();
    }

    //초기 설정
    abstract void init();

    //레이아웃에 컴포넌트 붙이기
    abstract void addComponent(TagNode tagNode);

    protected Drawable getDrawable(int id) {
        return ResourcesCompat.getDrawable(context.getResources(), id, null);
    }

    protected TextView makeTextView(int left, int top, int right, int bottom, int textSize) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);

        TextView textView = new TextView(context);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setLayoutParams(params);
        textView.setTextSize(textSize);
        return textView;
    }

    protected LinearLayout.LayoutParams getParams(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);

        return params;
    }


    protected ImageView makeImageView(String src) {
        LinearLayout.LayoutParams params = getParams(0, 23, 0, 20);
        ImageView imageView = new ImageView(linearLayout.getContext());
        Glide.with(linearLayout.getContext())
                .load(src)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);

        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(params);

        return imageView;
    }

    //레이아웃을 만드는 메소드
    protected LinearLayout makeLinearLayout(int left, int top, int right, int bottom, int orientation) {
        LinearLayout.LayoutParams params = getParams(left, top, right, bottom);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(orientation);

        return linearLayout;
    }

    //웹뷰를 만드는 메소드
    protected WebView makeWebView(int left, int top, int right, int bottom, int height, String src) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(left, top, right, bottom);
        WebView webView = new WebView(context);
        webView.setLayoutParams(params);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(src);

        return webView;
    }

    //문자열 변환
    protected String replaceAll(String text) {
        text = text.replaceAll("&gt;", ">");
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&nbsp;", " ");
        text = text.replaceAll("&amp;", "&");
        text = text.replaceAll("&quot;", "\"");

        return text;
    }

    //h1~h6 태그 사이즈 조절
    protected void textResize(TextView textView, int size) {
        int sz = (int) textView.getTextSize();
        switch (size) {
            case 1:
                sz = 25;
                break;
            case 2:
                sz = 20;
                break;
            case 3:
                sz = 17;
                break;
            case 4:
                sz = 14;
                break;
            case 5:
                sz = 12;
                break;
            case 6:
                sz = 10;
                break;
        }

        textView.setTextSize(sz);
    }

    protected String makeUrl(String data_id) {
        String ret = null;
        int end = data_id.indexOf("?");

        if (end > -1) {
            data_id = data_id.substring(0, end);
            ret = "https://oya.joins.com/bc_iframe.html?videoId=" + data_id;
        }else
            ret = data_id;

        return ret;
    }
}
