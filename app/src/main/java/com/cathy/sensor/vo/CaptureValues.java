package com.cathy.sensor.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import com.and.support.recyclerview.ObservableList;
import com.cathy.sensor.BR;
import com.cathy.sensor.presenter.FilePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xianggaofeng on 2018/3/20.
 */

public class CaptureValues extends BaseObservable {
    private List<CaptureValue> mValues = new ArrayList<>();
    private FilePresenter presenter = new FilePresenter();

    private ObservableList<Object> listPresenter;

    private boolean running;


    private Timer timer;
    private TimerTask timerTask;


    private String path;
    private int type;

    public CaptureValues(ObservableList<Object> listPresenter, int type) {
        this.listPresenter = listPresenter;
        timer = new Timer();
        this.type = type;
    }

    @Bindable
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        if (this.running == running) {
            return;
        }
        this.running = running;


        if (running) {

            if(timer==null){
                timer = new Timer();
            }
            timer.purge();
            int period;
            if (type == -1) {
                period = 1000;
            } else {
                period = 25;
            }
            timer.schedule(new CaptureTask(type), 50, period);

        } else {
            if(timerTask!=null){
                timerTask.cancel();
                timerTask=null;
            }
            if(timer!=null){
                timer.purge();
                timer.cancel();
                timer = null;

            }

        }
        notifyPropertyChanged(BR.running);
    }

    public void save(View view) {
        String dir = view.getContext().getExternalCacheDir().getAbsolutePath();
        String path = dir + File.separator + "excel";

        String name = "";
        if (type == -1) {
            name = "gps";
        } else if (type == Sensor.TYPE_GYROSCOPE) {
            name = "g";
        } else if (type == Sensor.TYPE_ACCELEROMETER) {
            name = "a";
        }
        String result = presenter.saveText(mValues, path, name);
        Toast.makeText(view.getContext(), "" + result, Toast.LENGTH_SHORT).show();
        if (result != null) {
            this.path = result;
        } else {
            this.path = null;
        }
    }

    public void reset() {
        setRunning(false);
        clear();
    }

    public void share(View view) {
        if (path == null) {
            return;
        }
        FilePresenter.shareFile(view.getContext(), new File(path));
//        String dir = view.getContext().getFilesDir().getPath();
//        presenter.saveExcel(mValues, dir);
    }

    @Bindable
    public int getCount() {
        return mValues.size();
    }

    public void add(CaptureValue value) {
        mValues.add(value);
        notifyPropertyChanged(BR.count);


    }

    public void clear() {
        mValues.clear();
        notifyPropertyChanged(BR.count);
    }


    public void cancel() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }


        reset();
    }

    public String getName() {
        if (type == -1) {
            return "GPS";
        } else if (type == Sensor.TYPE_ACCELEROMETER) {
            return "加速度";
        } else if (type == Sensor.TYPE_GYROSCOPE) {
            return "陀螺仪";
        }
        return "";
    }

    public class CaptureTask extends TimerTask {

        int type;

        public CaptureTask(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            List<Object> list = listPresenter;
            CaptureValue value = new CaptureValue();
            for (Object info :
                    list) {
                if (info instanceof SensorInfo) {

                    SensorEvent event = ((SensorInfo) info).getEvent();
                    if (event == null) {
                        return;
                    }
                    if (event.sensor.getType() == type) {
                        value.type = type;
                        value.time = ((SensorInfo) info).getTimestamp() + "";
                        value.x = event.values[0] + "";
                        value.y = event.values[1] + "";
                        value.z = event.values[2] + "";
                        value.name = event.sensor.getName() + "";
                    }
                } else if (info instanceof LocationInfo && type == -1) {


                    Location location = ((LocationInfo) info).getLocation();
                    if (location != null) {
                        value.time = location.getTime() + "";
                        value.x = location.getLongitude() + "";
                        value.y = location.getLatitude() + "";
                        value.z = location.getAltitude() + "";
                        value.name = "GPS";
                        value.type = type;
                    }
                    break;
                }
            }
            add(value);

        }
    }
}
