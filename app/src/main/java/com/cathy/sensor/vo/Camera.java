package com.cathy.sensor.vo;

public class Camera {

    android.hardware.Camera camera;

    public Camera() {
//        this.camera = android.hardware.Camera.open();
    }

    public android.hardware.Camera getCamera() {
        return camera;
    }
}
