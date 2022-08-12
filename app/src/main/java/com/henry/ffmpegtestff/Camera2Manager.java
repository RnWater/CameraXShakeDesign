package com.henry.ffmpegtestff;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.ImageReader;

/**
 * @author henry
 * @description
 * @date 2022/8/8.
 */
class Camera2Manager {
    private CameraManager cameraManager;
    private CameraCaptureSession cameraCaptureSession;
    private ImageReader render;
    private int previewWidth,previewHeight;
    private CameraDevice device;
    private CameraCaptureSession.CaptureCallback captureCallback;
    private int phoneDegree = 0;
    private static Camera2Manager camera2Manager;
    private Context context;
    private String mCameraId="0";
    private Camera2Manager(Context context){
        this.context = context;
        initCamera();
    }
    public static Camera2Manager getInstance(Context context){
        if (camera2Manager == null) {
            synchronized (Camera2Manager.class) {
                if (camera2Manager == null) {
                    camera2Manager = new Camera2Manager(context);
                }
            }
        }
        return camera2Manager;
    }
    public void initCamera(){
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = getCamera()[0];
            initDefaultPreviewSize();
            startOrientationListener();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startOrientationListener() {

    }

    private void initDefaultPreviewSize() {

    }

    /**
     * 获取摄像头
     * @return
     */
    private String[] getCamera() throws CameraAccessException {
        return cameraManager.getCameraIdList();
    }

}
