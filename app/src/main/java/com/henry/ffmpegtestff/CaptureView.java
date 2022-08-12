package com.henry.ffmpegtestff;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import androidx.annotation.Nullable;
/**
 * @author henry
 * @description
 * @date 2022/8/12.
 */
public class CaptureView extends View implements View.OnClickListener {
    private Paint paint;//画圆的画笔
    private float scaleTop=0.71f;//白色按钮与底部按钮的比例
    public CaptureView(Context context) {
        this(context, null);
    }
    public CaptureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        paint.setColor(Color.parseColor("#D3D3D3"));
        canvas.drawCircle(canvasWidth / 2, canvasHeight / 2, canvasHeight / 2, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(canvasWidth / 2, canvasHeight / 2, canvasHeight / 2*scaleTop, paint);
    }

    @Override
    public void onClick(View view) {
        AnimationSet animationSet =new AnimationSet(true);
        ScaleAnimation scaleAnimation =new ScaleAnimation(
                0, 0.1f,0,0.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(200);
        animationSet.addAnimation(scaleAnimation);
        this.startAnimation(animationSet);
    }
}
