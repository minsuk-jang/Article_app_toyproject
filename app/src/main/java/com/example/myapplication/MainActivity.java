package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        ImageButton google = (ImageButton)findViewById(R.id.google);
        google.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
    }
}