package com.example.myapplication.Parser;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

public abstract class BaseParser {
    protected LinearLayout linearLayout;
    protected String text;
    protected HtmlSpanner spanner;

    protected BaseParser(LinearLayout linearLayout, String text){
        this.linearLayout = linearLayout;
        this.text =text;
        spanner = new HtmlSpanner();
    }

    //초기 설정
    abstract void init();


    //이미지 핸들러
    protected class ImageTagHandler extends TagNodeHandler {
        @Override
        public void handleTagNode(TagNode node, final SpannableStringBuilder builder, final int start, final int end) {
            String src = node.getAttributes().get("src");

            if(src == null)
                src = node.getAttributeByName("data-src");

            builder.append("\n\ufff0\n");

            Drawable dr = getDrawable(src);
            ImageSpan span = new ImageSpan(dr);
            builder.setSpan(span, start+1,end+2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    //기기에 맞춰서 이미지의 사이즈를 확장시켜주는 메소드
    protected void setBounds(Drawable dr){
        Display display = ((WindowManager)linearLayout.getContext().getSystemService(linearLayout.getContext().WINDOW_SERVICE)).getDefaultDisplay();

        Point p = new Point();
        display.getSize(p);

        int maxWidth = p.x;
        dr.setBounds(0,0, maxWidth,(maxWidth*dr.getIntrinsicHeight())/dr.getIntrinsicWidth());

    }

    //텍스트 반환하는 메소드
    protected Spanned getString(){
        return spanner.fromHtml(text);
    }

    //이미지 반환
    protected Drawable getDrawable(String src){
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
                                .fitCenter()
                                .submit().get();

                        return dr;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(src).get(); //이미지를 동기적으로 얻는다.

            setBounds(dr);

            return dr;
        } catch (Exception e) {
            Drawable dr = ResourcesCompat.getDrawable(linearLayout.getResources(),R.drawable.ic_launcher_foreground,null);
            setBounds(dr);

            return dr;
        }
    }
}
