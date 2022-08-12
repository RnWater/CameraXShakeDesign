package com.henry.ffmpegtestff;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author henry
 * @description
 * @date 2022/8/4.
 */
public class ShakeDetector implements SensorEventListener {
    private static final int UPDATE_INTERVAL=1000;
    long mLastUpdateTime;
    private float mLastX,mLastY,mLastZ;
    Context mContext;
    SensorManager mSensorManager;
    ArrayList<OnShakeListener> mListeners;
    public int shakeThreshold=10;
    CameraTopView cameraTopView;
    public ShakeDetector(Context context,CameraTopView cameraTopView){
        this.mContext = context;
        this.cameraTopView = cameraTopView;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mListeners = new ArrayList<>();
    }
    public void registerOnShakeListener(OnShakeListener shakeListener){
        if (mListeners.contains(shakeListener)) {
            return;
        }
        mListeners.add(shakeListener);
    }
    public void removeShakeListener(OnShakeListener shakeListener){
        if (mListeners.contains(shakeListener)) {
            mListeners.remove(shakeListener);
        }
    }
    public void start(){
        if (mSensorManager == null) {
            throw new UnsupportedOperationException("设备不支持");
        }
        Sensor defaultSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (defaultSensor == null) {
            throw new UnsupportedOperationException("设备不支持");
        }
        boolean success = mSensorManager.registerListener(this, defaultSensor, SensorManager.SENSOR_DELAY_GAME);
        if (!success) {
            throw new UnsupportedOperationException();
        }
    }
    public void stop(){
        if (mSensorManager == null) {
            mSensorManager.unregisterListener(this);
        }
        cameraTopView = null;
        mSensorManager = null;
        mListeners.clear();
        mListeners = null;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;
        if (diffTime < UPDATE_INTERVAL)
            return;
        mLastUpdateTime = currentTime;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        double delta = Math.sqrt(deltaX * deltaX + deltaY *deltaY+
                deltaZ * deltaZ) / diffTime * 10000;
        if (delta > shakeThreshold) {// 当加速度的差值大于指定认为这是一个摇晃
            if (mListeners != null&&mListeners.size()>0) {
                notifyListeners();
            }
            if (cameraTopView != null) {
                cameraTopView.updateState(1);
            }
        } else {
            if (cameraTopView != null) {
                cameraTopView.updateState(0);
            }
        }
//1：sensor回调是一个持续的过程取何种算法判断抖动
//2: 抖动结束多久可以将按钮置位可点击状态
    }
    public void notifyListeners(){
        for (OnShakeListener listener : mListeners) {
            listener.onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
