package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<FragmentVO> list = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm, int behavior){
        super(fm,behavior);
    }

    public void addFragment(int icon, String title, Fragment fragment){
        list.add(new FragmentVO(title,fragment,icon));
    }

    public FragmentVO getFragmentVo(int idx){
        return list.get(idx);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position).fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).title;
    }
}
