package com.and.support.recyclerview;


public abstract class TypeProvider<T> {
    private TypeStore store = new TypeStore();

    public abstract int getItemViewType(T item);


    ViewBinder getViewBinder(int type) {
        return store.getViewBinder(type);
    }

    public void addViewBinder(int type, ViewBinder viewBinder) {
        store.addViewBinder(type, viewBinder);
    }
}