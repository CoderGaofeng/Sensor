package com.cathy.sensor.vo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

    @Bindable
    public long getTime() {
        if(location!=null){
           return location.getTime();
        }
        return -1;
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        }else {
            locationManager.removeUpdates(locationListener);
        }

        notifyPropertyChanged(BR.running);
    }
}
