package com.and.support.recyclerview.tools;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.and.support.recyclerview.ViewBinder;
import com.and.support.recyclerview.ViewHolder;

import java.util.List;

/**
 * Created by xianggaofeng on 2018/3/1.
 */

public class SimpleViewBinder<T> extends ViewBinder<T, ViewHolder> {
    private int mLayoutId;

    public SimpleViewBinder(int layoutId) {
        mLayoutId = layoutId;
    }


    @Override
    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        ViewHolder holder = new ViewHolder(inflater.inflate(mLayoutId, parent, false));
        onViewCreated(holder);
        return holder;
    }

    public void onViewCreated(ViewHolder holder) {
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, List payloads) {
        bindItem(holder, holder.<T>getItem());
    }


    public void bindItem(ViewHolder holder, Object item) {

    }
}
