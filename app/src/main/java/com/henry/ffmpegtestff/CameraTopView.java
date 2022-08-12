package com.henry.ffmpegtestff;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.logging.Logger;

/**
 * @author henry
 * @description
 * @date 2022/8/10.
 */
public class CameraTopView extends View {
    private Paint paint;//画背景的画笔
    private Paint tipPaint;//画提示框的画笔
    private TextPaint textPaint;//画文字的画笔
    private int emptyWidth,emptyHeight;//空白框的宽度和高度
    private int left=0;//空白框左边起始点
    private int top=0;//空白框上边起始点
    public CameraTopView(Context context) {
        super(context);
    }

    public CameraTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        tipPaint = new Paint();
        tipPaint.setAntiAlias(true);
        tipPaint.setColor(Color.parseColor("#cc000000"));

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(DisplayUtil.dipToPx(context,12));
        textPaint.setTextAlign(Paint.Align.CENTER);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        emptyWidth = canvasWidth - DisplayUtil.dipToPx(getContext(), 44);
        emptyHeight = canvasHeight - DisplayUtil.dipToPx(getContext(), 82 + 82 + 50);
        top = DisplayUtil.dipToPx(getContext(),82);
        left = (canvasWidth - emptyWidth)/2;
        //正常绘制黄色的圆形
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0,canvasWidth, canvasHeight, paint);
        if (shake) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.WHITE);
        }

        canvas.drawRoundRect(left, top, emptyWidth+left, emptyHeight+top, 40, 40, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(Color.RED);

        canvas.drawRoundRect(left+20, top+20, emptyWidth+left-20, emptyHeight+top-20,30,30, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        //View禁用掉GPU硬件加速，切换到软件渲染模式 必须加 不加部分手机会有显示问题
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        int shakeTipLeft = canvasWidth / 2 - 250;
        int shakeTipTop = top + emptyHeight / 2-60;
        if (screenState == 0) {
            canvas.rotate(0, shakeTipLeft+250, shakeTipTop+60);
        }else {
            canvas.rotate(90, shakeTipLeft+250, shakeTipTop+60);
        }
        if (shake) {
            String textTop = "画面过于抖动,";
            String textBottom = "请您保持手机稳定！";
            RectF rect = new RectF(canvasWidth / 2 - 250, top + emptyHeight / 2 - 60, canvasWidth / 2 + 250, top + emptyHeight / 2 + 120);
            canvas.drawRoundRect(rect, 20, 20, tipPaint);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float topText = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottomText = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            int baseLineY = (int) (rect.centerY() - topText/2 - bottomText/2);//基线中间点的y轴计算公式
            canvas.drawText(textTop,rect.centerX(),baseLineY-22,textPaint);
            canvas.drawText(textBottom,rect.centerX()+12,baseLineY+22,textPaint);
        }
        canvas.restoreToCount(layerId);
    }

    /**
     * 获取图片的实际位置和大小
     * @return
     */
    public RectF getReality(){
        return new RectF(left + 20, top + 20, emptyWidth + left - 20, emptyHeight + top - 20);
    }
    private boolean shake;
    public void updateState(int i) {
        if (i > 0) {
            shake = true;
        } else {
            shake = false;
        }
    }
    private int screenState;//0 竖屏 1 横屏
    public void setScreenState(int screenState){
        this.screenState = screenState;
    }
}
