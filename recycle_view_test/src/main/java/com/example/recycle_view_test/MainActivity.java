package com.example.recycle_view_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycle);

        List<UsersVO> list = new ArrayList<>();

        for(int i =0 ; i < 20 ; i++){
            String name = "User" +i;
            String email = name + "@gmail.com";

            list.add(new UsersVO(name,email));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleAdapter(list,recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadListener(new onLoadListener() {
            @Override
            public void onLoad(List<UsersVO> list) {
                //새로운 데이터를 불러 오기
                list.add(null);
                adapter.notifyItemInserted(list.size()-1);

                new Handler().postDelayed(new Data(list),5000);
            }
        });


    }

    private class Data implements Runnable{
        private List<UsersVO> list;
        public  Data(List<UsersVO> list){
            this.list =list;
        }

        @Override
        public void run() {
            list.remove(list.size() -1);
            int index = list.size();
            int end = index + 20;

            for(int i = index ; i< end ; i++){
                String name = "User"+ i;
                String email = name + "gmail.com";
                list.add(new UsersVO(name,email));
            }

            adapter.notifyDataSetChanged();
            adapter.setLoad();
        }
    }
}
