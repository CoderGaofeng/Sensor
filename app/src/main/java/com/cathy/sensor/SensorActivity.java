package com.cathy.sensor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.and.support.recyclerview.DataBoundAdapter;
import com.and.support.recyclerview.DataBoundViewHolder;
import com.and.support.recyclerview.ObservableAdapter;
import com.and.support.recyclerview.ObservableList;
import com.and.support.recyclerview.tools.SimpleViewBound;
import com.cathy.sensor.databinding.ActivitySensorBinding;
import com.cathy.sensor.vo.CaptureValues;
import com.cathy.sensor.vo.LocationInfo;
import com.cathy.sensor.vo.SensorInfo;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends DataBoundActivity<ActivitySensorBinding> {
    private static final String TAG = "sensor";
    private SensorManager mSensorManager;


    private LocationManager mLocationManager;
    private CaptureValues mValues;


    private ObservableList<Object> presenter = new ObservableList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mValues.cancel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ObservableAdapter adapter = new ObservableAdapter(presenter);
        adapter.addViewBinder(SensorInfo.class, new SimpleViewBound(BR.data, R.layout.item_sensor))
                .addViewBinder(LocationInfo.class, new SimpleViewBound(BR.data, R.layout.item_gps))
                .addViewBinder(CaptureValues.class, new SimpleViewBound(BR.data, R.layout.item_capture_task));

        binding.recyclerView.setAdapter(adapter);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        mValues = new CaptureValues(adapter.getItems());
        checkLocation();
        presenter.insert(mValues);

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void checkLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            String[] strings = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA

            };
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


    final void onCreateSensorMenu(SensorInfo info) {


        presenter.insert(info);
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

    final List<SensorInfo> requestSensorList() {
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        List<SensorInfo> sensorInfos = new ArrayList<>();
        for (Sensor sensor :
                sensors) {
            SensorInfo info = new SensorInfo(manager, sensor);
            sensorInfos.add(info);
        }

        return sensorInfos;
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
                List<Object> list = presenter;
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
                List<Object> list = presenter;
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
                List<Object> list = presenter;
                for (Object info :
                        list) {
                    if (info instanceof SensorInfo) {
                        ((SensorInfo) info).onClickCapture();
                    }

                }
                break;
            }
            case R.id.action_reset: {
                List<Object> list = presenter;
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

            case R.id.action_time:
                mValues.setRunning(!mValues.isRunning());


                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Toast.makeText(this, "获得数据：" + data.getStringExtra("value"), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void showInputDialog() {
        RecyclerView recyclerView = new RecyclerView(this);
        final Dialog dialog = new AlertDialog.Builder(this)
                .setView(recyclerView)
                .create();

        ViewGroup viewGroup;
        DataBoundAdapter adapter = new DataBoundAdapter();
        adapter.addViewBinder(SensorInfo.class, new SimpleViewBound(BR.data, R.layout.item_sensor_select) {
            @Override
            public void onDataBoundCreated(DataBoundViewHolder vh) {
                super.onDataBoundCreated(vh);
                vh.binding.getRoot().setOnClickListener(v -> {
                    dialog.dismiss();
                    onCreateSensorMenu(vh.getItem());
                });
            }
        });
        adapter.display(requestSensorList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


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
