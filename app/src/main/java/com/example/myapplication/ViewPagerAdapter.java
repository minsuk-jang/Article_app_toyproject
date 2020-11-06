package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.DataVO.FragmentVO;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentVO> list = new ArrayList<>();
    private FragmentManager manager;
    public ViewPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
        manager = fm;
    }

    public void addFragment(int icon, String title, Fragment fragment) {
        list.add(new FragmentVO(title, fragment, icon));
    }

    public FragmentVO getFragmentVo(int idx) {
        return list.get(idx);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle();
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        ArticleFragment articleFragment = (ArticleFragment)object;


        if(articleFragment.getFragmentChange()){

            return POSITION_NONE;
        }else
            return POSITION_UNCHANGED;

    }
}
