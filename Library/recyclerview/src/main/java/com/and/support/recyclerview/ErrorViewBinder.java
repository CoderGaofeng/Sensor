package com.and.support.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ErrorViewBinder extends ViewBinder<Object, ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        ViewHolder holder = new ViewHolder(textView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, List<Object> payloads) {
        super.onBindViewHolder(holder, payloads);

        TextView textView = (TextView) holder.itemView;
        textView.setText(holder.getItem() + "");
    }
}
