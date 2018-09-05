package com.cathy.sensor.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.hardware.SensorEvent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cathy.sensor.R;
import com.cathy.sensor.vo.CaptureEvent;


public class SensorInfoCard extends LinearLayout {


    private TextView x;
    private TextView y;
    private TextView z;
    private TextView ms;
    private TextView ns;

    public SensorInfoCard(Context context) {
        this(context, null);
    }

    public SensorInfoCard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorInfoCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        View.inflate(context, R.layout.sensor_info_card, this);
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);
        ms = findViewById(R.id.ms);
        ns = findViewById(R.id.ns);
    }


    @BindingAdapter({"captureEvent"})
    public static void setSensorEvent(SensorInfoCard card, CaptureEvent event) {

        if (event == null) {
            card.reset();
            return;
        }
        float[] values = event.values;
        if (values == null) {
            card.reset();
            return;
        }

        card.x.setText(String.valueOf(values[0]));
        card.y.setText(String.valueOf(values[1]));
        card.z.setText(String.valueOf(values[2]));
        card.ms.setText(String.valueOf(System.currentTimeMillis()));
        card.ns.setText(String.valueOf(System.nanoTime()));


    }


    private static final String PLACE_HOLDER = "";

    public void reset() {
        x.setText(PLACE_HOLDER);
        y.setText(PLACE_HOLDER);
        z.setText(PLACE_HOLDER);
        ms.setText(PLACE_HOLDER);
        ns.setText(PLACE_HOLDER);
    }

    @BindingAdapter({"sensorEvent"})
    public static void setSensorEvent(SensorInfoCard card, SensorEvent event) {

        if (event == null) {
            card.reset();
            return;
        }
        float[] values = event.values;
        if (values == null) {
            card.reset();
            return;
        }

        if (values.length != 3) {
            float[] old = new float[3];
            for (int i = 0; i < values.length; i++) {
                old[i] = values[i];
            }
            values = old;
        }
        card.x.setText(String.valueOf(values[0]));
        card.y.setText(String.valueOf(values[1]));
        card.z.setText(String.valueOf(values[2]));
        card.ms.setText(String.valueOf(System.currentTimeMillis()));
        card.ns.setText(String.valueOf(System.nanoTime()));


    }

}
