package com.example.recycle_view_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_USER = 1;
    private final int VIEW_PROGESS = 2;
    private List<UsersVO> list;
    private onLoadListener onLoadListener;
    private int visibleThreshold =5;
    private int lastVisibleItem, totlaItemCount;
    private boolean isLoading = false;

    public RecycleAdapter(List<UsersVO> l, RecyclerView recyclerView){
        this.list = l;
        final LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totlaItemCount = manager.getItemCount();
                lastVisibleItem = manager.findLastVisibleItemPosition();

                if(!isLoading && totlaItemCount <= lastVisibleItem+visibleThreshold){
                    if(onLoadListener != null)
                        onLoadListener.onLoad(list);
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadListener(onLoadListener onLoadListener){
        this.onLoadListener = onLoadListener;
    }

    public void setLoad(){
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_USER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_activity,parent,false);
            return new UserHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_activity,parent,false);
            return new ProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof UserHolder){
            ((UserHolder) holder).name.setText(list.get(position).name);
            ((UserHolder) holder).email.setText(list.get(position).email);
        }else if(holder instanceof ProgressHolder){
            ((ProgressHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_PROGESS : VIEW_USER;
    }
}
