package com.and.support.recyclerview;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public abstract class AdapterDelegate {

    private static ErrorViewBinder defaultViewBinder = new ErrorViewBinder();
    private LayoutInflater inflater;
    TypeProvider provider;

    public AdapterDelegate(TypeProvider provider) {
        this.provider = provider;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewBinder binder = provider.getViewBinder(viewType);
        if (binder == null) {
            binder = defaultViewBinder;
        }
        assert inflater != null;
        return binder.onCreateViewHolder(inflater, parent);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        ViewBinder binder = provider.getViewBinder(holder.getItemViewType());
        if (binder == null) {
            binder = defaultViewBinder;
        }
        holder.setItem(getItem(position));
        binder.onBindViewHolder(holder, payloads);
    }

    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        ViewBinder binder = provider.getViewBinder(holder.getItemViewType());
        if (binder == null) {
            binder = defaultViewBinder;
        }
        binder.onViewAttachedToWindow(holder);
    }

    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        ViewBinder binder = provider.getViewBinder(holder.getItemViewType());
        if (binder == null) {
            binder = defaultViewBinder;
        }
        binder.onViewDetachedFromWindow(holder);
    }

    public abstract int getItemCount();

    public abstract Object getItem(int position);

    public int getItemViewType(int position) {
        return provider.getItemViewType(getItem(position));
    }
}
