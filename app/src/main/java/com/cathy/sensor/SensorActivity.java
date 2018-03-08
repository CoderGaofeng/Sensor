package com.cathy.sensor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.cathy.sensor.databinding.ActivitySensorBinding;
import com.cathy.sensor.vo.SensorInfo;
import com.prayxiang.support.recyclerview.ListPresenter;
import com.prayxiang.support.recyclerview.tools.SimpleViewBound;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends DataBoundActivity<ActivitySensorBinding> {
    private static final String TAG = "sensor";
    private SensorManager mSensorManager;


    private ListPresenter<SensorInfo> presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenter = ListPresenter.<SensorInfo>create()
                .addViewBinder(SensorInfo.class, new SimpleViewBound(BR.data, R.layout.item_sensor))
                .attachWithBound(binding.recyclerView)
                .display(new ArrayList<>());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sensor;
    }

    final void onCreateSensorMenu(int type) {
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(type);
        if (sensor == null) {
            Toast.makeText(this, "不支持类型", Toast.LENGTH_LONG).show();
            return;
        }
        SensorInfo info = new SensorInfo(manager, sensor);

        presenter.insert(info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_start: {
                List<SensorInfo> list = presenter.getItems();
                for (SensorInfo info :
                        list) {
                    info.setRunning(true);
                }

                break;
            }
            case R.id.action_stop: {
                List<SensorInfo> list = presenter.getItems();
                for (SensorInfo info :
                        list) {
                    info.setRunning(false);
                }
                break;
            }
            case R.id.action_capture: {
                List<SensorInfo> list = presenter.getItems();
                for (SensorInfo info :
                        list) {
                    info.onClickCapture();
                }
                break;
            }
            case R.id.action_reset: {
                List<SensorInfo> list = presenter.getItems();
                for (SensorInfo info :
                        list) {
                    info.onClickReset();
                }
                break;
            }
            case R.id.action_accelerometer:
                onCreateSensorMenu(Sensor.TYPE_ACCELEROMETER);
                break;


            case R.id.action_gyroscope:
                onCreateSensorMenu(Sensor.TYPE_GYROSCOPE);
                break;

            case R.id.action_custom:
                showInputDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showInputDialog() {
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(40);
        final Dialog dialog = new AlertDialog.Builder(this)
                .setView(picker)
                .setPositiveButton("确定", (d, which) -> onCreateSensorMenu(picker.getValue()))
                .create();
        dialog.show();
        binding.getRoot().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                dialog.dismiss();
            }
        });

    }

}
