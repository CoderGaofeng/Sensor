package com.and.support.recyclerview;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ClassTypeProvider extends TypeProvider {
    private List<Class<?>> types = new ArrayList<>();

    public int getItemViewType(Object item) {
        int type = types.indexOf(item.getClass());
        if (type == -1) {
            Log.d("type", "not fount class = " + item.getClass());
        }
        return type;
    }

    public void addViewBinder(Class<?> cls, ViewBinder viewBinder) {
        int index = types.indexOf(cls);
        if (index == -1) {
            types.add(cls);
            index = types.size() - 1;
        }
        addViewBinder(index, viewBinder);
    }
}
