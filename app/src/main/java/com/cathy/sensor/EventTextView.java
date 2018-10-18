package com.cathy.sensor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class EventTextView extends android.support.v7.widget.AppCompatTextView {
    private String content;
    public EventTextView(Context context) {
        super(context);
    }

    public EventTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        content = event.toString();
        content+="\n";
        content+="index = "+ event.getActionIndex();
        content+="\n";
        content+="indexId = "+ event.getPointerId(event.getActionIndex());

        setText(content);
        return true;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(content!=null){
            setText(content+"\n"+event);

        }
        return super.dispatchKeyEvent(event);
    }
}
