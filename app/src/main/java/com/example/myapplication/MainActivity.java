package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//기사의 화면을 보여주는 액티비티
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private long pressedTime =0 ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ArrayList<String> article_list = (ArrayList<String>)getIntent().getSerializableExtra("article_list");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        init();
    }

    //초기 설정
    private void init(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),1);

        //todo 수정필요
        viewPagerAdapter.addFragment(R.drawable.joongang,"중앙 일보",new oneFragment());
        viewPagerAdapter.addFragment(R.drawable.donga,"동아 일보",new oneFragment());
        viewPagerAdapter.addFragment(R.drawable.google,"구글 기사",new oneFragment());

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        for(int i =0 ; i <viewPager.getAdapter().getCount(); i++)
            tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentVo(i).icon);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_appbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(pressedTime == 0){
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        }else{
            long end = System.currentTimeMillis();
            if(end - pressedTime > 2000){ //누른지 2초가 지난 후,
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = end;
            }else{
                super.onBackPressed();
                finish();
            }
        }
    }
}