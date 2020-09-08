package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.icu.text.RelativeDateTimeFormatter;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.DataVO.ArticleVO;
import com.example.myapplication.Parser.DongaParser;
import com.example.myapplication.Parser.ParserHelper;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.TagNodeHandler;
import net.nightwhistler.htmlspanner.handlers.ImageHandler;

import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ShowArticleActivity extends AppCompatActivity {
    TextView title;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_show_article);
        linearLayout = (LinearLayout) findViewById(R.id.container);

        Intent intent = getIntent();

        String s = intent.getStringExtra("subject");
        String t = intent.getStringExtra("title");
        String c = intent.getStringExtra("content");

        title = (TextView) findViewById(R.id.title);

        title.setText(t);
        ParserHelper.attachCompoent(linearLayout,s,c);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
