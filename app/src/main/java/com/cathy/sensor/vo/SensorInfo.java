package com.cathy.sensor.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.cathy.sensor.BR;

public class SensorInfo extends BaseObservable {

    protected SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            setEvent(event);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    protected SensorManager manager;
    private Sensor sensor;
    private SensorEvent event;
    private CaptureEvent captureEvent;
    private long startTime;
    private long endTime;

    public SensorInfo(SensorManager manager, Sensor sensor) {
        this.manager = manager;
        this.sensor = sensor;
    }

    @Bindable
    public SensorEvent getEvent() {
        return event;
    }

    public void setEvent(SensorEvent event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
        notifyPropertyChanged(BR.timestamp);
    }


    public String getTitle() {
        return sensor.getName();
    }

    @Bindable
    public CaptureEvent getCaptureEvent() {
        return captureEvent;
    }


    @Bindable
    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        notifyPropertyChanged(BR.startTime);
    }

    @Bindable
    public long getStartTime() {
        return startTime;
    }

    @Bindable
    public long getEndTime() {
        return endTime;
    }


    public void setEndTime(long endTime) {
        this.endTime = endTime;
        notifyPropertyChanged(BR.endTime);
    }

    public void setCaptureEvent(CaptureEvent captureEvent) {
        this.captureEvent = captureEvent;
        notifyPropertyChanged(BR.captureEvent);
    }

    public void onClickCapture() {
        if (getEvent() != null) {
            setCaptureEvent(new CaptureEvent(getEvent()));
        }

    }


    public void onClickReset() {
        setRunning(false);
        setCaptureEvent(null);
        setEvent(null);
        setStartTime(0);
        setEndTime(0);

    }

    public void onChangeRunningState() {
        setRunning(!isRunning());
    }


    @Bindable
    public String getRunningState() {
        if (!running) {
            return "开始";
        } else {
            return "停止";
        }
    }


    private boolean running;


    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
            setStartTime(System.currentTimeMillis());
            setEndTime(0);
        } else {
            manager.unregisterListener(listener);
            setEndTime(System.currentTimeMillis());

        }
        notifyPropertyChanged(BR.runningState);
    }

    public boolean isRunning() {
        return running;
    }


}
