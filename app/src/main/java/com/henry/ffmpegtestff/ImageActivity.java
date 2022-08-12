package com.henry.ffmpegtestff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageActivity extends AppCompatActivity {
    private int width;
    private int top;
    private int height;
    private String filePath;
    private TextView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        width=getIntent().getIntExtra("width", 0);
        top=getIntent().getIntExtra("top", 0);
        height=getIntent().getIntExtra("height", 0);
        filePath=getIntent().getStringExtra("filePath");
        image = findViewById(R.id.image);
        image.setOnClickListener(view -> {
            Intent intent = new Intent(ImageActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}