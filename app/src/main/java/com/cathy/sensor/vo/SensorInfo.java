package com.cathy.sensor.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.MotionEvent;

import com.cathy.sensor.BR;

import java.lang.reflect.Field;
import java.util.Arrays;

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

    public Sensor getSensor() {
        return sensor;
    }

    @Bindable
    public SensorEvent getEvent() {

        return event;
    }

    public void setEvent(SensorEvent event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
        notifyPropertyChanged(BR.timestamp);
        notifyPropertyChanged(BR.content);
    }


    @Bindable
    public String getContent(){
        if(event==null){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Field[] fields = SensorEvent.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            builder.append("\n");
            Field field = fields[i];
            try {
                builder.append("\"" + field.getName() + "\" = ");

                Object object = field.get(event);

                if (object.getClass().isArray()){
                    if(object instanceof Object[]){
                        builder.append(Arrays.toString((Object[]) object));
                    } else if (object instanceof boolean[]) {
                        builder.append(Arrays.toString((boolean[]) object));
                    } else if (object instanceof byte[]) {
                        builder.append(Arrays.toString((byte []) object));
                    } else if (object instanceof char[]) {
                        builder.append(Arrays.toString((char[]) object));
                    } else if (object instanceof double[]) {
                        builder.append(Arrays.toString((double []) object));
                    } else if (object instanceof float[]) {
                        builder.append(Arrays.toString((float []) object));
                    } else if (object instanceof int[]) {
                        builder.append(Arrays.toString((int []) object));
                    } else if (object instanceof long[]) {
                        builder.append(Arrays.toString((long []) object));
                    } else if (object instanceof short[]) {
                        builder.append(Arrays.toString((short []) object));
                    }


                }else {
                    builder.append(object+"");
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }

        Sensor sensor = event.sensor;
        if(sensor!=null){
            builder.append("\n");
            builder.append("\"" + "getFifoMaxEventCount()" + "\" = ");
            builder.append(sensor.getFifoMaxEventCount()+"");
            builder.append("\n");
            builder.append("\"" + "getFifoReservedEventCount()" + "\" = ");
            builder.append(sensor.getFifoReservedEventCount()+"");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.append("\n");
                builder.append("\"" + "getHighestDirectReportRateLevel()" + "\" = ");
                builder.append(sensor.getHighestDirectReportRateLevel()+"");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.append("\n");
                builder.append("\"" + "getId()" + "\" = ");
                builder.append(sensor.getId()+"");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.append("\n");
                builder.append("\"" + "getMaxDelay()" + "\" = ");
                builder.append(sensor.getMaxDelay()+"");
            }
        }
        return builder.toString();
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
//        if(event!=null){
//            return event.timestamp;
//        }
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
