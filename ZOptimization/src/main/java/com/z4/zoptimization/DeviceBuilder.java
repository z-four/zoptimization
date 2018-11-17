package com.z4.zoptimization;

import android.content.Context;
import android.util.DisplayMetrics;

import static com.z4.zoptimization.Utils.getCurrentDisplayMetrics;
import static com.z4.zoptimization.Utils.getDensity;
import static com.z4.zoptimization.Utils.getScaledDensity;
import static com.z4.zoptimization.Utils.isNull;

/**
 * Created by z4.
 */

public final class DeviceBuilder {
    private static final int DEFAULT_SCREEN_VALUE = 1;

    private int mCurrentDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mCurrentDisplayHeight = DEFAULT_SCREEN_VALUE;
    private int mTestedDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mTestedDisplayHeight = DEFAULT_SCREEN_VALUE;

    private float mTestedDeviceDensity = -1;
    private float mTestedDeviceScaledDensity = -1;
    private float mCurrDeviceDensity;
    private float mCurrDeviceScaledDensity;

    public DeviceBuilder applyDp(float density) {
        mTestedDeviceDensity = density;
        return this;
    }

    public DeviceBuilder applySp(float scaledDensity) {
        mTestedDeviceScaledDensity = scaledDensity;
        return this;
    }

    public DeviceBuilder currentScreen(int width, int height) {
        mCurrentDisplayWidth = width;
        mCurrentDisplayHeight = height;
        return this;
    }

    public DeviceBuilder testedScreen(int width, int height) {
        mTestedDisplayWidth = width;
        mTestedDisplayHeight = height;
        return this;
    }

    void configure(Context context) {
        if (mCurrentDisplayWidth == DEFAULT_SCREEN_VALUE ||
                mCurrentDisplayHeight == DEFAULT_SCREEN_VALUE) {
            determinateDisplaySize(context);
        }
        mCurrDeviceDensity = getDensity(context);
        mCurrDeviceScaledDensity = getScaledDensity(context);
    }

    private void determinateDisplaySize(Context context) {
        if (isNull(context)) return;

        DisplayMetrics metrics = getCurrentDisplayMetrics(context);
        mCurrentDisplayWidth = metrics.widthPixels;
        mCurrentDisplayHeight = metrics.heightPixels;
    }

    int getCurrentDisplayWidth() { return mCurrentDisplayWidth; }

    int getCurrentDisplayHeight() { return mCurrentDisplayHeight; }

    int getTestedDisplayWidth() { return mTestedDisplayWidth; }

    int getTestedDisplayHeight() { return mTestedDisplayHeight; }

    float getTestedDeviceDensity() {
        return mTestedDeviceDensity;
    }

    float getTestedDeviceScaledDensity() {
        return mTestedDeviceScaledDensity;
    }

    float getCurrDeviceDensity() {
        return mCurrDeviceDensity;
    }

    float getCurrDeviceScaledDensity() {
        return mCurrDeviceScaledDensity;
    }
}