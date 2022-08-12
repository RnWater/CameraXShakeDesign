package com.henry.ffmpegtestff;
import android.app.Application;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import java.util.concurrent.Executors;
/**
 * @author henry
 * @description
 * @date 2022/8/12.
 */
public class MyApplication extends Application implements CameraXConfig.Provider {
    @Override
    public CameraXConfig getCameraXConfig() {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
//                .setCameraExecutor(Executors.newSingleThreadExecutor())
                .build();
    }
}
