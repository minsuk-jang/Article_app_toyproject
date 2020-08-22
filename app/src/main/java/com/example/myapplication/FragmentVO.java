package com.example.myapplication;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

public class FragmentVO {
    String title;
    Fragment fragment;
   int icon;

    public FragmentVO(String title, Fragment fragment, int icon) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
    }
}
