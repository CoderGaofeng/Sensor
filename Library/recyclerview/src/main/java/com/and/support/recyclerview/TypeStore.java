package com.and.support.recyclerview;

import android.util.SparseArray;


public class TypeStore {

    private SparseArray<ViewBinder> mCaches = new SparseArray<>();

    ViewBinder getViewBinder(int type) {
        return mCaches.get(type);
    }

    public void addViewBinder(int type, ViewBinder viewBinder) {
        mCaches.put(type, viewBinder);
    }


}
