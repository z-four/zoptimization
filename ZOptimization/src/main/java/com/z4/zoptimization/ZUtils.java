package com.z4.zoptimization;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ZUtils {

    static boolean checkIfNotZero (float value) {
        return value > 0;
    }

    static boolean isNull(Object... object) {
        boolean isNull = false;
        int objectsCount = object.length;

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

    static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getCurrentDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getCurrentDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}