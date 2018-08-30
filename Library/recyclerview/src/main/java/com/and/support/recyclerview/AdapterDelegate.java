package com.and.support.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public abstract class AdapterDelegate extends RecyclerView.Adapter<ViewHolder> {

    private static ErrorViewBinder defaultViewBinder = new ErrorViewBinder();
    private LayoutInflater inflater;
    TypeProvider provider;

    public AdapterDelegate(TypeProvider provider) {
        this.provider = provider;
    }

    @NonNull
    @Override
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        ViewBinder binder = provider.getViewBinder(holder.getItemViewType());
        if (binder == null) {
            binder = defaultViewBinder;
        }
        holder.setItem(getItem(position));
        binder.onBindViewHolder(holder, payloads);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        ViewBinder binder = provider.getViewBinder(holder.getItemViewType());
        if (binder == null) {
            binder = defaultViewBinder;
        }
        binder.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        ViewBinder binder = provider.getViewBinder(holder.getItemViewType());
        if (binder == null) {
            binder = defaultViewBinder;
        }
        binder.onViewDetachedFromWindow(holder);
    }

    public abstract int getItemCount();

    public abstract Object getItem(int position);

}
