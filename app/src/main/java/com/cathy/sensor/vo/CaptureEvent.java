package com.cathy.sensor.vo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;



public class CaptureEvent {

    public final float[] values;

    /**
     * The sensor that generated this event. See
     * {@link android.hardware.SensorManager SensorManager} for details.
     */
    public Sensor sensor;

    /**
     * The accuracy of this event. See {@link android.hardware.SensorManager
     * SensorManager} for details.
     */
    public int accuracy;

    /**
     * The time in nanosecond at which the event happened
     */
    public long timestamp;
    public long time;

    public CaptureEvent(SensorEvent event) {

        if(event==null){
            values=new float[3];
            return;
        }
        values = new float[event.values.length];
        System.arraycopy(event.values,0,values,0,values.length);
        sensor = event.sensor;
        accuracy = event.accuracy;
        timestamp = event.timestamp;
    }
}
