package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Display;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;


public class GlideImageGetter implements Html.ImageGetter {

    private Context context;
    private TextView textView;

    public GlideImageGetter(Context context, TextView target) {
        this.context = context;
        textView = target;
    }

    @Override
    public Drawable getDrawable(String url) {
        BitmapDrawablePlaceholder drawable = new BitmapDrawablePlaceholder();
        RequestOptions myOptions = new RequestOptions()
                .fitCenter(); // centerInside // 이옵션 없으면 사진 최대사이즈로 들어감

        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(myOptions)
                .into(drawable);

        return drawable;
    }

    private class BitmapDrawablePlaceholder extends BitmapDrawable implements Target<Bitmap> {

        protected Drawable drawable;

        BitmapDrawablePlaceholder() {
            super(context.getResources(), Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
        }

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        private void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();
            int maxWidth = textView.getMeasuredWidth();
            if (drawableWidth > maxWidth) {
                int calculatedHeight = maxWidth * drawableHeight / drawableWidth;
                drawable.setBounds(0, 0, maxWidth, calculatedHeight);
                setBounds(0, 0, maxWidth, calculatedHeight);
            } else {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight);
                setBounds(0, 0, drawableWidth, drawableHeight);
            }
            textView.setText(textView.getText());
        }

        @Override
        public void onLoadStarted(@Nullable Drawable placeholderDrawable) {
            if (placeholderDrawable != null) {
                setDrawable(placeholderDrawable);
            }
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            if (errorDrawable != null) {
                setDrawable(errorDrawable);
            }
        }

        @Override
        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
            setDrawable(new BitmapDrawable(context.getResources(), bitmap));

        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholderDrawable) {
            if (placeholderDrawable != null) {
                setDrawable(placeholderDrawable);
            }
        }

        @Override
        public void getSize(@NonNull final SizeReadyCallback cb) {
            textView.post(new Runnable() {
                              @Override
                              public void run() {
                                  cb.onSizeReady(textView.getWidth(), textView.getHeight());
                              }
                          }

            );
        }

        @Override
        public void removeCallback(@NonNull SizeReadyCallback cb) {
        }

        @Override
        public void setRequest(@Nullable Request request) {
        }

        @Nullable
        @Override
        public Request getRequest() {
            return null;
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onDestroy() {
        }

    }
}

// http://blog.naver.com/PostView.nhn?blogId=tkddlf4209&logNo=221560262472&redirect=Dlog&widgetTypeCall=true&directAccess=false

