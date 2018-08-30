package com.and.support.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public class ObservableAdapter extends DataBoundAdapter<Object> implements ListUpdateCallback {
    private ObservableList<Object> items;
    private RecyclerView.OnScrollListener listener;

    public ObservableAdapter(ObservableList<Object> items) {
        this.items = items;
        setItems(items);
    }

    public ObservableAdapter(ObservableList<Object> items, RecyclerView.OnScrollListener listener) {
        this.items = items;
        setItems(items);
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        items.removeListChangeCallback(this);
        items.addOnListChangeCallback(this);
        if(listener!=null){
            recyclerView.removeOnScrollListener(listener);
            recyclerView.addOnScrollListener(listener);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        items.removeListChangeCallback(this);
        if(listener!=null){
            recyclerView.removeOnScrollListener(listener);
        }
    }

    @Override
    public ObservableList<Object> getItems() {
        return items;
    }

    @Override
    public void onChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onInserted(int position, int count) {
        notifyItemRangeChanged(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        notifyItemRangeChanged(position, count, payload);
    }
}
