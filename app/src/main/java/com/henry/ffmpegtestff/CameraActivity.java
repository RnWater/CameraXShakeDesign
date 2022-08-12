package com.henry.ffmpegtestff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraState;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;

import com.google.common.util.concurrent.ListenableFuture;
import com.henry.ffmpegtestff.databinding.ActivityCameraBinding;
import com.henry.ffmpegtestff.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity{
    private Preview preview;
    private ExecutorService mExecutorService;
    private ProcessCameraProvider cameraProvider;
    private Camera camera;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        previewView = findViewById(R.id.previewCamera);
        mExecutorService = Executors.newSingleThreadExecutor(); // 创建一个单线程线程池

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        previewView.post(new Runnable() {
            @Override
            public void run() {
                imageCapture=getImageCapture();
                cameraProviderFuture.addListener(() -> {
                    try {
                        cameraProvider = cameraProviderFuture.get();
                        bindPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, ContextCompat.getMainExecutor(CameraActivity.this));
            }
        });
    }

    private void bindPreview() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            preview = new Preview.Builder().
                    setTargetResolution(new Size(1080,1920)).
                    build();
            CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview,imageCapture);
            preview.setSurfaceProvider(previewView.getSurfaceProvider());
        }
    }

    public ImageCapture getImageCapture(){
        return new ImageCapture.Builder().
                setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).
                setTargetResolution(new Size(1080, 1920)).
                setTargetRotation(previewView.getDisplay().getRotation()).
                build();
    }
}