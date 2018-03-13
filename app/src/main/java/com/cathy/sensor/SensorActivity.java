package com.cathy.sensor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.cathy.sensor.databinding.ActivitySensorBinding;
import com.cathy.sensor.vo.LocationInfo;
import com.cathy.sensor.vo.SensorInfo;
import com.prayxiang.support.recyclerview.ListPresenter;
import com.prayxiang.support.recyclerview.tools.SimpleViewBound;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends DataBoundActivity<ActivitySensorBinding> {
    private static final String TAG = "sensor";
    private SensorManager mSensorManager;


    private ListPresenter<Object> presenter;
    private LocationManager mLocationManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenter = ListPresenter.<Object>create()
                .addViewBinder(SensorInfo.class, new SimpleViewBound(BR.data, R.layout.item_sensor))
                .addViewBinder(LocationInfo.class, new SimpleViewBound(BR.data, R.layout.item_gps))
                .attachWithBound(binding.recyclerView)
                .display(new ArrayList<>());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
//        mLocationManager.getBestProvider(createFineCriteria(),true);

        checkLocation();

    }

    public void checkLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this,
                    strings, 2);
        } else {
           presenter.insert(new LocationInfo(mLocationManager));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result :
                grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        checkLocation();


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
                List<Object> list = presenter.getItems();
                for (Object info :
                        list) {
                    if (info instanceof SensorInfo) {
                        ((SensorInfo) info).setRunning(true);
                    } else if (info instanceof LocationInfo) {
                        ((LocationInfo) info).setRunning(true);
                    }

                }

                break;
            }
            case R.id.action_stop: {
                List<Object> list = presenter.getItems();
                for (Object info :
                        list) {
                    if (info instanceof SensorInfo) {
                        ((SensorInfo) info).setRunning(false);
                    } else if (info instanceof LocationInfo) {
                        ((LocationInfo) info).setRunning(false);
                    }

                }


                break;
            }
            case R.id.action_capture: {
                List<Object> list = presenter.getItems();
                for (Object info :
                        list) {
                    if (info instanceof SensorInfo) {
                        ((SensorInfo) info).onClickCapture();
                    }

                }
                break;
            }
            case R.id.action_reset: {
                List<Object> list = presenter.getItems();
                for (Object info :
                        list) {
                    if (info instanceof SensorInfo) {
                        ((SensorInfo) info).onClickReset();
                    }

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
