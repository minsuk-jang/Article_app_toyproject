package com.example.myapplication.Holder;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class progressHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public progressHolder(@NonNull View itemView) {
        super(itemView);

        progressBar = (ProgressBar)itemView.findViewById(R.id.progress_circular);

    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

}
