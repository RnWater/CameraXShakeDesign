package com.henry.ffmpegtestff;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author henry
 * @description
 * @date 2022/8/10.
 */
public class ShapeTextView extends AppCompatTextView {
    private boolean openSelector;
    private boolean circularAngle;//是否启用圆角 不需要传递具体的圆角值
    //自定背景边框Drawable
    private GradientDrawable gradientDrawable;
    //按下时的Drawable
    private GradientDrawable selectorDrawable;
    //背景色
    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
        gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                solidColor,null,0, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        setBackground(gradientDrawable);
        invalidate();
    }

    public void setGradientColor(int startColor,int centerColor,int endColor) {
        this.startColor = startColor;
        this.centerColor = centerColor;
        this.endColor = endColor;
        //渐变色背景
        if (centerColor==0){
            gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidColor,new int[]{startColor,endColor},orientation, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        }else{
            gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidColor,new int[]{startColor,centerColor,endColor},orientation, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        }
        setBackground(gradientDrawable);
        invalidate();
    }
    //今日任务按钮专用
    public void resetShape(int strokeColor){
        this.resetShape(strokeColor,solidColor);
    }
    //今日任务按钮专用
    public void resetShape(int strokeColor,int solidColor){
        this.resetShape(strokeColor,strokeWidth,solidColor);
    }
    //今日任务按钮专用
    public void resetShape(int strokeColor,int strokeWidth,int solidColor){
        this.resetShape(strokeColor,strokeWidth,solidColor,radius);
    }
    //今日任务按钮专用
    public void resetShape(int strokeColor,int strokeWidth,int solidColor,float radius){
        this.solidColor = solidColor;
        this.strokeColor = strokeColor;
        this.radius = radius;
        this.strokeWidth = strokeWidth;
        gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                solidColor,null,0, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        setBackground(gradientDrawable);
        invalidate();
    }
    //改变圆角大小
    public void resetShapeRadius(float radius){
        this.radius = radius;
        gradientDrawable = getNeedDrawable(new float[]{radius, radius, radius, radius,
                        radius, radius, radius, radius},
                solidColor,null,0, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        setBackground(gradientDrawable);
        invalidate();
    }
    //填
    // 充色
    private int solidColor = 0;
    //边框色
    private int strokeColor = 0;
    //按下填充色
    private int solidTouchColor = 0;
    //按下边框色
    private int strokeTouchColor = 0;
    //按下字体色
    private int textTouchColor = 0;
    //边框宽度
    private int strokeWidth = 0;
    //四个角的弧度
    private float radius;
    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;
    //边框虚线的宽度
    float dashWidth = 0;
    //边框虚线的间隙
    float dashGap = 0;
    //字体色
    private int textColor = 0;

    private int press_or_select =0;

    //是否开启渐变
    private boolean enableGradient  = false;

    private int defaultStartColor = Color.parseColor("#E5E5E5");
    private int defaultCenterColor = 0;
    private int defaultEndColor = Color.parseColor("#343434");
    private int startColor = 0;
    private int centerColor = 0;
    private int endColor = 0;

    private int orientation = 0;
    /**渐变色方向*/
    private static final int LEFT_RIGHT = 0;
    private static final int TOP_BOTTOM = 1;
    private static final int RIGHT_LEFT = 2;
    private static final int BOTTOM_TOP = 3;
    private static final int TL_BR = 4;
    private static final int BL_TR = 5;
    private static final int TR_BL = 6;
    private static final int BR_TL = 7;

//    private static Context mContext;

    /**渐变类型*/
    private static int gradientType = 0;
    /**线性渐变*/
    private static final int LINEAR = 0;
    /**放射性渐变（圆形渐变），起始颜色从cenralX,centralY点开始*/
    private static final int RADIAL = 1;
    /**扫描式渐变（扇形渐变）。*/
    private static final int SWEEP = 2;

    /**渐变颜色的半径,需要设置GradientType*/
    private float gradientRadius = 0;


    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        mContext = context;
        init(context, attrs);
        if (enableGradient){
            //渐变色背景
            if (centerColor==0){
                gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                                bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                        solidColor,new int[]{startColor,endColor},orientation, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
            }else{
                gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                                bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                        solidColor,new int[]{startColor,centerColor,endColor},orientation, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
            }
        }else{
            //默认背景
            gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidColor,null,0, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        }

        //如果设置了选中时的背景
        if (openSelector) {
            selectorDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidTouchColor,null,0, strokeWidth, strokeTouchColor, dashWidth, dashGap,gradientType,gradientRadius);

            //动态生成Selector
            StateListDrawable stateListDrawable = new StateListDrawable();
            int[] colors = new int[]{textTouchColor, textColor};
            int[][] states = new int[2][];
            int check;
            if (press_or_select == 0) {
                states[0] = new int[]{android.R.attr.state_selected, android.R.attr.state_selected};
                check = android.R.attr.state_selected;
            } else {
                states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_pressed};
                check = android.R.attr.state_pressed;
            }
            states[1] = new int[]{};
            ColorStateList colorDrawable = new ColorStateList(states,colors);
            //是否按下
            stateListDrawable.addState(new int[]{check}, selectorDrawable);
            stateListDrawable.addState(new int[]{}, gradientDrawable);

            setBackground(stateListDrawable);
            setTextColor(colorDrawable);
        } else {
            setBackground(gradientDrawable);
        }
        setOnClickListener(v -> {

        });
    }

    /**
     * 初始化参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeTextView, 0, 0);

        press_or_select = ta.getInteger(R.styleable.ShapeTextView_shape_press_select, 0);
        openSelector = ta.getBoolean(R.styleable.ShapeTextView_shape_openSelector, false);
        solidColor = ta.getInteger(R.styleable.ShapeTextView_shape_solidColor, 0x00000000);
        strokeColor = ta.getInteger(R.styleable.ShapeTextView_shape_strokeColor, 0x00000000);

        solidTouchColor = ta.getInteger(R.styleable.ShapeTextView_shape_solidTouchColor, 0x00000000);
        strokeTouchColor = ta.getInteger(R.styleable.ShapeTextView_shape_strokeTouchColor, 0x00000000);
        textTouchColor = ta.getInteger(R.styleable.ShapeTextView_shape_textTouchColor, 0x00000000);
        textColor = ta.getInteger(R.styleable.ShapeTextView_shape_textColor, 0x00000000);
        strokeWidth = (int) ta.getDimension(R.styleable.ShapeTextView_shape_strokeWidth, 0);

        //四个角单独设置会覆盖radius设置
        radius = ta.getDimension(R.styleable.ShapeTextView_shape_radius, 0);
        topLeftRadius = ta.getDimension(R.styleable.ShapeTextView_shape_topLeftRadius, radius);
        topRightRadius = ta.getDimension(R.styleable.ShapeTextView_shape_topRightRadius, radius);
        bottomLeftRadius = ta.getDimension(R.styleable.ShapeTextView_shape_bottomLeftRadius, radius);
        bottomRightRadius = ta.getDimension(R.styleable.ShapeTextView_shape_bottomRightRadius, radius);

        dashGap = ta.getDimension(R.styleable.ShapeTextView_shape_dashGap, 0);
        dashWidth = ta.getDimension(R.styleable.ShapeTextView_shape_dashWidth, 0);

        startColor = ta.getColor(R.styleable.ShapeTextView_gradient_startColor,defaultStartColor);
        centerColor = ta.getColor(R.styleable.ShapeTextView_gradient_centerColor,defaultCenterColor);
        endColor = ta.getColor(R.styleable.ShapeTextView_gradient_endColor,defaultEndColor);
        enableGradient = ta.getBoolean(R.styleable.ShapeTextView_gradient_enableGradient,false);
        orientation = ta.getInt(R.styleable.ShapeTextView_gradient_orientation,LEFT_RIGHT);
        gradientType = ta.getInt(R.styleable.ShapeTextView_gradient_type,LINEAR);
        gradientRadius = ta.getFloat(R.styleable.ShapeTextView_gradient_radius,0);
        circularAngle = ta.getBoolean(R.styleable.ShapeTextView_shape_circular_angle,false);
        ta.recycle();
    }

    /**
     * @param radius      四个角的半径
     * @param colors      渐变的颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @return
     */
    public static GradientDrawable getNeedDrawable(float[] radius, int[] colors, int strokeWidth, int strokeColor) {
        //TODO:判断版本是否大于16  项目中默认的都是Linear散射 都是从左到右 都是只有开始颜色和结束颜色
        GradientDrawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            drawable = new GradientDrawable();
            drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            drawable.setColors(colors);
        } else {
            drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        }

        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    /**
     * @param radius      四个角的半径
     * @param bgColor     背景颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @return
     */
    public static GradientDrawable getNeedDrawable(float[] radius, int bgColor, int strokeWidth, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(bgColor);
        return drawable;
    }

    /**
     * @param radius      四个角的半径
     * @param bgColor     背景颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @param dashWidth   虚线边框宽度
     * @param dashGap     虚线边框间隙
     * @return
     */
    public static GradientDrawable getNeedDrawable(float[] radius, int bgColor,int []colors,int gradientOrientation, int strokeWidth, int strokeColor, float dashWidth, float dashGap,int gradientType,float gradientRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
        if (colors==null||colors.length==0){
            drawable.setColor(bgColor);
        }else{
            drawable.setColors(colors);
            drawable.setGradientType(gradientType);
            switch (gradientOrientation){
                case LEFT_RIGHT:
                    drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                    break;
                case RIGHT_LEFT:
                    drawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                    break;
                case TOP_BOTTOM:
                    drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                    break;
                case BOTTOM_TOP:
                    drawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                    break;

            }
            drawable.setGradientRadius(gradientRadius);
        }

        return drawable;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (circularAngle) {
            resetRadius(h/2);
        }
    }

    //改变圆角大小
    public void resetRadius(float radius){
        this.radius = radius;
        gradientDrawable = getNeedDrawable(new float[]{radius, radius, radius, radius,
                        radius, radius, radius, radius},
                solidColor,null,0, strokeWidth, strokeColor, dashWidth, dashGap,gradientType,gradientRadius);
        setBackground(gradientDrawable);
    }
}
