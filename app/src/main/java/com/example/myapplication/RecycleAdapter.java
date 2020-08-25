package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataVO.articleVO;
import com.example.myapplication.Holder.articleHolder;
import com.example.myapplication.Holder.progressHolder;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_PROGRESS = 2;
    private final int VIEW_ARTICLE = 1;
    private int totalViewCount, lastVisibleItem;
    private List<articleVO> list;
    private boolean isLoading = false;
    private onLoadListener onLoadListener;
    private Context context;

    public RecycleAdapter(RecyclerView recyclerView, Context context) {
        this.context = context;
        final LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            //todo 화면 개선
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dx == 0 && dy == 0)
                    return;

                totalViewCount = manager.getItemCount();
                lastVisibleItem = manager.findLastVisibleItemPosition();
                Log.d("jms","total Count : " + totalViewCount + " Last : " + lastVisibleItem);
                if(!isLoading && totalViewCount == lastVisibleItem ){
                    if(onLoadListener != null){
                        onLoadListener.onLoad(totalViewCount);
                    }

                    isLoading = true;
                }
            }
        });
    }

    public void setList(List<articleVO> list) {
        this.list = list;
    }

    public void setOnLoadListener(onLoadListener onLoadListener){
        this.onLoadListener =onLoadListener;
    }

    public void setLoad(){
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_ARTICLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_part,parent,false);
            return new articleHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_part,parent,false);
            return new progressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof progressHolder){
            ((progressHolder) holder).getProgressBar().setIndeterminate(true);
        }else if(holder instanceof articleHolder){
            Glide.with(context).load(list.get(position).getImg_url()).placeholder(R.drawable.ic_launcher_foreground).into(((articleHolder) holder).getImageView()); //이미지 넣기
            ((articleHolder) holder).getTitle().setText(list.get(position).getTitle());
            ((articleHolder) holder).getContent().setText(list.get(position).getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_PROGRESS : VIEW_ARTICLE;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : totalViewCount;
    }

    public void setSize(int size){
        this.totalViewCount = Math.min(size,list.size());
    }

}
