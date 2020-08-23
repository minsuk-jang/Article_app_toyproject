package com.example.recycle_view_test;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgressHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public ProgressHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = (ProgressBar)itemView.findViewById(R.id.progress_circular);
    }
}
