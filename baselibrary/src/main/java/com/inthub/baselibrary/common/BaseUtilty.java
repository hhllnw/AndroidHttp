package com.inthub.baselibrary.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

/**
 * Created by hhl on 2016/9/1.
 * 常用工具类方法
 */
public class BaseUtilty {

    private static float screenDensity = 0;

    public static boolean isNull(String str) {
        if (str == null || "".equals(str))
            return true;
        else return false;
    }

    public static boolean isNotNull(String str) {
        if (str != null && !"".equals(str))
            return true;
        else return false;
    }

    public static void saveStringToMainSP(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(BaseComParams.SP_NAME, 0);
        sp.edit().putString(key, value).commit();
    }

    public static String getStringFromMainSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(BaseComParams.SP_NAME, 0);
        return sp.getString(key, "");
    }

    /**
     * 获取屏幕 密度
     */
    public static float getScreenDensity(Context context) {
        if (screenDensity <= 0) {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                dm = context.getResources().getDisplayMetrics();
                screenDensity = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
            } catch (Exception e) {
            }
        }
        return screenDensity;
    }

    /**
     * 根据手机的分辨率从dip的单位转成为px(像素)
     */
    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getScreenDensity(context));
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位转成为dip
     */
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context));
    }

}
