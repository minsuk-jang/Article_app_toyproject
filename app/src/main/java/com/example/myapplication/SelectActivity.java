package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton google, donga, kukmin, ytn, joongang, chosun, khan;

    /*
    2개로 설정
    만약, 3개로 지정할 경우, 1번 -> 3번으로 갈 경우, 1번의 Fragment가 다시 생성됨
    안드로이드 폰 내부에서 모든걸 처리하므로 위와 같이 새롭게 진행될 때마다 크롤링을 진행하면 성능이 떨어질 거 같아 2개로 설정
     */
    private final int MAX_SELECTED = 2;
    private Button confirm;
    private ArrayList<String> ret; //다음 액티비티로 전송될 리스트


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        ret = new ArrayList<>();

        init();
    }


    //초기화
    private void init() {
        google = (ImageButton)findViewById(R.id.google);
        google.setOnClickListener(this);
        donga = (ImageButton)findViewById(R.id.donga);
        donga.setOnClickListener(this);
        kukmin = (ImageButton)findViewById(R.id.kukmin);
        kukmin.setOnClickListener(this);
        joongang = (ImageButton)findViewById(R.id.joongang);
        joongang.setOnClickListener(this);
        ytn = (ImageButton)findViewById(R.id.ytn);
        ytn.setOnClickListener(this);
        chosun = (ImageButton)findViewById(R.id.chosun);
        chosun.setOnClickListener(this);
        khan = (ImageButton)findViewById(R.id.khan);
        khan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v== confirm){
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("article_list",ret);
            startActivity(intent);

            finish();
        }else if (ret.size() < MAX_SELECTED) {
            if (!v.isSelected()) {
                ret.add(getButtonName(v));
            } else
                ret.remove(getButtonName(v));
            v.setSelected(!v.isSelected());
        } else {
            if (v.isSelected()) {
                v.setSelected(!v.isSelected());
                ret.remove(getButtonName(v));
            }
        }

        if(ret.size() >= 1){
            confirm.setVisibility(View.VISIBLE);
        }else
            confirm.setVisibility(View.INVISIBLE);
    }

    //선택된 버튼의 이름을 반환
    private String getButtonName(View v) {
        String ret = null;
        if (v == google) {
            ret = "google";
        } else if (v == chosun)
            ret = "chosun";
        else if (v == ytn)
            ret = "ytn";
        else if (v == joongang)
            ret = "joongang";
        else if (v == kukmin)
            ret = "kukmin";
        else if (v == donga)
            ret = "donga";
        else if (v == khan)
            ret = "khan";

        return ret;
    }
}
