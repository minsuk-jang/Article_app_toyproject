package com.example.myapplication.Holder;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ProgressHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public ProgressHolder(@NonNull View itemView) {
        super(itemView);

        progressBar = (ProgressBar)itemView.findViewById(R.id.progress_circular);

    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

}
