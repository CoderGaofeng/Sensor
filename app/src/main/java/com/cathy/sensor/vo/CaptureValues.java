package com.cathy.sensor.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.view.View;

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

    public CaptureValues(ObservableList<Object> listPresenter) {
        this.listPresenter = listPresenter;
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

        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new CaptureTask();
        }
        if (running) {
            timer.purge();
            timer.schedule(timerTask, 50, 50);
        } else {

            timerTask.cancel();
            timerTask = null;
            timer.purge();
        }

        notifyPropertyChanged(BR.running);
    }

    public void save(View view) {
        String dir = view.getContext().getExternalCacheDir().getAbsolutePath();
        String path = dir + File.separator + "excel";

        String result = presenter.saveExcel(mValues, path);
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

    public class CaptureTask extends TimerTask {

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
                    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        value.accelerometerTime = ((SensorInfo) info).getTimestamp() + "";
                        value.aX = event.values[0] + "";
                        value.aY = event.values[1] + "";
                        value.aZ = event.values[2] + "";
                    } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                        value.gyroscopeTime = ((SensorInfo) info).getTimestamp() + "";
                        value.gX = event.values[0] + "";
                        value.gY = event.values[1] + "";
                        value.gZ = event.values[2] + "";

                    }


                } else if (info instanceof LocationInfo) {

                    Location location = ((LocationInfo) info).getLocation();
                    if (location != null) {
                        value.gpsTime = location.getTime() + "";
                        value.latitude = location.getLatitude() + "";
                        value.longitude = location.getLongitude() + "";
                        value.altitude = location.getAltitude()+"";
                    }
                }

            }
            add(value);

        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}
