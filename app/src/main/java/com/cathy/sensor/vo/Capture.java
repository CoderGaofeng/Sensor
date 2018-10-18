package com.cathy.sensor.vo;

import android.databinding.ObservableField;
import android.hardware.Sensor;

import com.and.support.recyclerview.ObservableList;

public class Capture {
    private ObservableList list;

    public Capture(ObservableList list) {
        this.list = list;
        values.set(new CaptureValues(list, -1));
        gValues.set(new CaptureValues(list, Sensor.TYPE_GYROSCOPE));
        aValues.set(new CaptureValues(list,Sensor.TYPE_ACCELEROMETER));
        mValues.set(new CaptureValues(list,Sensor.TYPE_MAGNETIC_FIELD));
    }

    private ObservableField<CaptureValues> values = new ObservableField<>();
    private ObservableField<CaptureValues> gValues = new ObservableField<>();
    private ObservableField<CaptureValues> aValues = new ObservableField<>();
    private ObservableField<CaptureValues> mValues = new ObservableField<>();

    public ObservableField<CaptureValues> getMValues() {
        return mValues;
    }

    public ObservableList getList() {
        return list;
    }

    public void setList(ObservableList list) {
        this.list = list;
    }

    public ObservableField<CaptureValues> getValues() {
        return values;
    }

    public void setValues(ObservableField<CaptureValues> values) {
        this.values = values;
    }

    public ObservableField<CaptureValues> getGValues() {
        return gValues;
    }

    public void setgValues(ObservableField<CaptureValues> gValues) {
        this.gValues = gValues;
    }

    public ObservableField<CaptureValues> getAValues() {
        return aValues;
    }

    public void setaValues(ObservableField<CaptureValues> aValues) {
        this.aValues = aValues;
    }

    public void cancel() {
        values.get().cancel();
        gValues.get().cancel();
        aValues.get().cancel();
        mValues.get().cancel();
    }

    public boolean isRunning() {
        return values.get().isRunning();
    }

    public void setRunning(boolean running){
        values.get().setRunning(running);
        gValues.get().setRunning(running);
        aValues.get().setRunning(running);
        mValues.get().setRunning(running);

    }

}
