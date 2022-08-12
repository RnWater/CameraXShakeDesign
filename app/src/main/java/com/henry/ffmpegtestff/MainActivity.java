package com.henry.ffmpegtestff;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.google.common.util.concurrent.ListenableFuture;
import com.henry.ffmpegtestff.databinding.ActivityMainBinding;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class MainActivity extends AppCompatActivity implements CameraXConfig.Provider {
    static {
        System.loadLibrary("ffmpegtestff");
    }
    private PreviewView previewView;
    private ActivityMainBinding binding;
    private ProcessCameraProvider cameraProvider;
    private Preview preview;
    private ConstraintLayout camera_content;
    private ShapeTextView capture;
    private ShapeTextView captureTop;
    private ConstraintLayout parent;
    private ImageCapture imageCapture;
    private CameraTopView cameraTopView;
    private MediaPlayer shootMP;
    private ImageView backImage;
    private ShakeDetector shakeDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //状态栏的处理，透明并和页面重叠
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        camera_content = findViewById(R.id.camera_content);
        previewView = new PreviewView(this);
        previewView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        camera_content.addView(previewView);
        parent = findViewById(R.id.parent);
        backImage = findViewById(R.id.backImage);
        cameraTopView = findViewById(R.id.cameraTop);
        ActivityCompat.requestPermissions(
                this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                }, 123
        );
        capture = findViewById(R.id.capture);
        captureTop = findViewById(R.id.captureTop);
        preview = new Preview.Builder().
                setTargetResolution(new Size(1080,1920)).
                build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        mMediaDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        mExecutorService = Executors.newSingleThreadExecutor(); // 创建一个单线程线程池

        previewView.post(() -> initCamera());
        shakeDetector = new ShakeDetector(this,cameraTopView);
        shakeDetector.registerOnShakeListener(() -> {
        });
        shakeDetector.start();
        captureTop.setOnClickListener(view -> {
            shootSound();
            showScaleAnimation();
            takePicture();
        });
        myOrientationListener = new MyOrientationListener(this,preview,capture,parent);
        myOrientationListener.enable();
        backImage.setOnClickListener(view -> finish());
        binding.captureView.setOnClickListener(view -> {
            shootSound();
            AnimationSet animationSet =new AnimationSet(true);
            ScaleAnimation scaleAnimation =new ScaleAnimation(
                    1.0f, 0.8f,1.0f,0.8f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(200);
            animationSet.addAnimation(scaleAnimation);
            binding.captureView.startAnimation(animationSet);
            takePicture();
        });
    }
    private void initCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        imageCapture=getImageCapture();
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                if (cameraProvider != null) {
                    cameraProvider.unbindAll();
                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                    cameraProvider.bindToLifecycle(MainActivity.this, cameraSelector, imageCapture, preview);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },ContextCompat.getMainExecutor(this));
    }

    private void showScaleAnimation() {
        AnimationSet animationSet =new AnimationSet(true);
        ScaleAnimation scaleAnimation =new ScaleAnimation(
                0, 0.1f,0,0.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(200);
        animationSet.addAnimation(scaleAnimation);
        captureTop.startAnimation(animationSet);
        capture.startAnimation(animationSet);
    }

    /**
     *   播放系统拍照声音
     */
    public void shootSound(){
        AudioManager meng = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
        if (volume != 0){
            if (shootMP == null)
                shootMP = MediaPlayer.create(this, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (shootMP != null)
                shootMP.start();
        }
    }
    MyOrientationListener myOrientationListener;
    @Override
    public void finish() {
        //销毁时取消监听
        shakeDetector.stop();
        myOrientationListener.onStop();
        super.finish();
    }

    private String mPhotoPath; // 照片保存路径
    // 获取照片的保存路径
    public String getPhotoPath() {
        return mPhotoPath;
    }

    private ExecutorService mExecutorService; // 声明一个线程池对象
    private String mMediaDir; // 媒体保存目录
    // 开始拍照
    public void takePicture() {
        mPhotoPath = String.format("%s/%s.jpg", mMediaDir, DateUtil.getNowDateTime());
        ImageCapture.Metadata metadata = new ImageCapture.Metadata();
        // 构建图像捕捉器的输出选项
        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(new File(mPhotoPath))
                .setMetadata(metadata).build();
        // 执行拍照动作
        imageCapture.takePicture(options, mExecutorService, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                Log.e("我的拍照", "拍照完成" + mPhotoPath);
            }
            @Override
            public void onError(ImageCaptureException exception) {
                Log.e("我的拍照", "拍摄失败，错误信息为："+exception.getMessage());
            }
        });
    }

    public ImageCapture getImageCapture(){
        return new ImageCapture.Builder().
                setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).
                setTargetResolution(new Size(1080, 1920)).
                setTargetRotation(previewView.getDisplay().getRotation()).
                build();
    }
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig()).setMinimumLoggingLevel(Log.ERROR).build();
    }
}