package com.cathy.sensor.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cathy.sensor.R;
import com.cathy.sensor.databinding.ItemCaptureCardBinding;
import com.cathy.sensor.vo.CaptureValues;

public class CaptureCard extends LinearLayout {
    private ItemCaptureCardBinding binding;

    public CaptureCard(Context context) {
        super(context);
    }

    public CaptureCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_capture_card, this, true);
//        View.inflate(context, R.layout.item_capture_card, this);
    }


    @BindingAdapter({"captureValue"})
    public static void setCaptureValue(CaptureCard captureCard, ObservableField<CaptureValues> values) {
        if (values != null) {
            captureCard.binding.setData(values.get());
        }
    }
}
