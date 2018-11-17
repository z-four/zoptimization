package com.z4.zoptimization;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

abstract class Utils {

    static boolean isEmpty (float value) {
        return value <= 0;
    }

    static boolean isNull(Object... object) {
        boolean isNull = false;
        final int objectsCount = object.length;

        for(int i = 0; i < objectsCount; i++) {
            if(object[i] == null) {
                isNull = true;
                break;
            }
        }
        return isNull;
    }

    static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    static DisplayMetrics getCurrentDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        return metrics;
    }

    static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }
}