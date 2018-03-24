package com.cathy.sensor.vo;

import android.annotation.SuppressLint;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.cathy.sensor.BR;

/**
 * Created by xianggaofeng on 2018/3/13.
 */

public class LocationInfo extends BaseObservable {
    LocationManager locationManager;
    String TAG = "location";

    public LocationInfo(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: "+location);
            LocationInfo.this.location = location;
            notifyPropertyChanged(BR.time);
            notifyPropertyChanged(BR.altitude);
            notifyPropertyChanged(BR.latitude);
            notifyPropertyChanged(BR.longitude);
            notifyPropertyChanged(BR.systemTime);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: "+extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: "+provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: "+provider);
        }
    };
    boolean running;

    private Location location;
    private long lastTime;
    private long interval;

    @Bindable
    public long getTime() {
        if(location!=null){
           return location.getTime();
        }
        return -1;
    }
    @Bindable
    public long getSystemTime(){
        long time = System.currentTimeMillis();
        interval = time - lastTime;
        notifyPropertyChanged(BR.interval);
        lastTime = time;
        return time;
    }

    public Location getLocation() {
        return location;
    }

    @Bindable
    public long getInterval(){
        return interval;
    }
    @Bindable
    public boolean isRunning() {
        return running;
    }

    @SuppressLint("MissingPermission")
    public void setRunning(boolean running) {
        this.running = running;
        Log.d("xgf",running+"");
        if(running){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, -1, 0, locationListener);

        }else {
            locationManager.removeUpdates(locationListener);
        }

        notifyPropertyChanged(BR.running);
    }


    @Bindable
    public String getAltitude() {
        if(location!=null){
            return location.getAltitude()+"";
        }
        return null;
    }

    @Bindable
    public String getLatitude() {
        if(location!=null){
            return location.getLatitude()+"";
        }
        return null;
    }

    @Bindable
    public String getLongitude() {
        if(location!=null){
            return location.getLongitude()+"";
        }
        return null;
    }
}
