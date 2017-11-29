package com.z4.zoptimization;

import android.content.Context;

import static com.z4.zoptimization.Utils.getDensity;
import static com.z4.zoptimization.Utils.getScaledDensity;

/**
 * Created by z4.
 */

public class ConfigBuilder {
    private float mTestedDeviceDensity = -1;
    private float mTestedDeviceScaledDensity = -1;

    private float mCurrDeviceDensity;
    private float mCurrDeviceScaledDensity;

    public ConfigBuilder applyDp(float density) {
        mTestedDeviceDensity = density;
        return this;
    }

    public ConfigBuilder applySp(float scaledDensity) {
        mTestedDeviceScaledDensity = scaledDensity;
        return this;
    }

    void configure(Context context) {
        mCurrDeviceDensity = getDensity(context);
        mCurrDeviceScaledDensity = getScaledDensity(context);
    }

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