package com.and.support.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by prayxiang on 2017/10/17.
 *
 * @description TODO
 */
@SuppressWarnings("all")
public class ItemViewBinder<T> extends ViewBinder<T, ViewHolder> {

    BaseAdapter adapter;

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    public final void onBindViewHolder(ViewHolder holder, List<Object> payloads) {
        bindItem(holder, (T) holder.getItem());
    }

    public void bindItem(ViewHolder holder, T item) {
    }


    public void onViewDetachedFromWindow(ViewHolder viewHolder) {

    }

    public void onViewAttachedToWindow(ViewHolder viewHolder) {
    }
}
