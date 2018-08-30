package com.and.middleware;

import android.content.Context;
import android.util.TypedValue;

public class AndContext {
    private static AndContext instance;

    private Context context;

    private void init(Context context) {
        instance = new AndContext(context.getApplicationContext());
    }

    public static AndContext getInstance() {
        return instance;
    }

    private AndContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public AndContext get() {
        return instance;
    }

    public int dpToPx(float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int) (px + 0.5f);
    }
}
