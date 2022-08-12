package com.henry.ffmpegtestff;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;

import androidx.camera.core.Preview;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

/**
 * @author henry
 * @description
 * @date 2022/8/10.
 */
public class MyOrientationListener extends OrientationEventListener {

    private static final String TAG = MyOrientationListener.class.getSimpleName();
    private Preview preview;
    private Activity context;
    private ShapeTextView capture;
    private ConstraintLayout parent;
    private ShapeTextView captureTop;
    private CameraTopView cameraTopView;
    public MyOrientationListener(Context context,Preview preview,ShapeTextView capture,ConstraintLayout parent) {
        this(context,0,preview,capture,parent);
    }

    public MyOrientationListener(Context context, int rate,Preview preview,ShapeTextView capture,ConstraintLayout parent) {
        super(context, rate);
        this.context = (Activity) context;
        this.preview = preview;
        this.capture = capture;
        this.parent = parent;
        captureTop = parent.findViewById(R.id.captureTop);
        cameraTopView = parent.findViewById(R.id.cameraTop);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        Log.d(TAG, "orention" + orientation);
        int previewOrientation=0;
        boolean landSpace = false;
        int screenOrientation = context.getResources().getConfiguration().orientation;
        if (((orientation >= 0) && (orientation < 45)) || (orientation >= 315)) {//设置竖屏
            previewOrientation=0;
//            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
//                Log.d(TAG, "设置竖屏");
//                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
            extracted(false);
        } else if (orientation >= 225 && orientation < 315) { //设置横屏
            previewOrientation = Surface.ROTATION_90;
            Log.d(TAG, "设置横屏");
//            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            }
            extracted(true);
        } else if (orientation >= 45 && orientation < 135) {// 设置反向横屏
            previewOrientation = Surface.ROTATION_270;
            Log.d(TAG, "反向横屏");
//            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
//                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//            }
            extracted(false);
        } else if (orientation >= 135 && orientation < 225) {
            previewOrientation = Surface.ROTATION_180;
            Log.d(TAG, "反向竖屏");
//            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
//                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//            }
            extracted(true);
        }
    }
    public void onStop(){
        cameraTopView = null;
        capture = null;
        parent= null;
        captureTop= null;
    }
    private void extracted(boolean landSpace) {
        if (landSpace) {
            if (cameraTopView != null) {
                cameraTopView.setScreenState(1);
            }
        } else {
            if (cameraTopView != null) {
                cameraTopView.setScreenState(0);
            }
        }
    }
}
