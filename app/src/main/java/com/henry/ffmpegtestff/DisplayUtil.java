package com.henry.ffmpegtestff;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author henry
 * @description
 * @date 2022/8/10.
 */
public class DisplayUtil {
    /**
     * convert px to its equivalent dp
     * 将px转换为与之相等的dp
     */
    public static int pxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dipToPx(Context context,float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }
    public static int sp2px(Context mContext,int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mContext.getResources().getDisplayMetrics());
    }

    // 隐藏软键盘  并从上滑到下面
    public static void hideSoftInputFromWindow(Context context, View view) {
        //
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取屏幕高度 px
     * @return
     */
    public static int getScreenHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度 px
     * @return
     */
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static int getDensityDpi(Context context){
        return context.getResources().getDisplayMetrics().densityDpi;
    }
}
