package com.and.support.recyclerview;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {


    AdapterDelegate delegate;

    private List<T> items = Collections.emptyList();

    public void setItems(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    protected TypeProvider provider;

    public BaseAdapter(TypeProvider provider) {
        this.provider = provider;
        delegate = new AdapterDelegate(provider) {
            @Override
            public int getItemCount() {
                return items.size();
            }

            @Override
            public Object getItem(int position) {
                return items.get(position);
            }

        };

    }

    public BaseAdapter() {
        this(new ClassTypeProvider());
    }

    public TypeProvider getProvider() {
        return provider;
    }

    @NonNull
    @Override
    @CallSuper
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegate.onCreateViewHolder(parent, viewType);
    }


    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        throw new IllegalArgumentException("just overridden to make final.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        delegate.onBindViewHolder(holder, position, payloads);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        delegate.onViewAttachedToWindow(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        delegate.onViewDetachedFromWindow(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return delegate.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }
}
