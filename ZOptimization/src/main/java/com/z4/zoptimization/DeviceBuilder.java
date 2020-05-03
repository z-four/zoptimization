package com.z4.zoptimization;

import android.content.Context;
import android.util.DisplayMetrics;

import static com.z4.zoptimization.Utils.getCurrentDisplayMetrics;
import static com.z4.zoptimization.Utils.getDensity;
import static com.z4.zoptimization.Utils.getScaledDensity;
import static com.z4.zoptimization.Utils.isNull;

/**
 * Provides a means to define optimization config.
 * which depends on user needs and tested device screen params.
 *
 * @author Dmitriy Zhyzhko
 */
public final class DeviceBuilder {
    private static final int DEFAULT_SCREEN_VALUE = 1;

    /**
     * Tested and current devices screen width and height.
     */
    private int mCurrentDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mCurrentDisplayHeight = DEFAULT_SCREEN_VALUE;
    private int mTestedDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mTestedDisplayHeight = DEFAULT_SCREEN_VALUE;

    /**
     * Tested and current devices screen density and scaled density.
     */
    private float mTestedDeviceDensity = -1;
    private float mTestedDeviceScaledDensity = -1;
    private float mCurrDeviceDensity;
    private float mCurrDeviceScaledDensity;

    /**
     * This method is used to apply tested device dp.
     *
     * @param density This is the device dp that user has tested ui for.
     * @return Builder's instance to make possible to add more config.
     */
    public DeviceBuilder applyDp(float density) {
        mTestedDeviceDensity = density;
        return this;
    }

    /**
     * This method is used to apply tested device sp.
     *
     * @param scaledDensity This is the device sp that user has tested ui for.
     * @return Builder's instance to make possible to add more config.
     */
    public DeviceBuilder applySp(float scaledDensity) {
        mTestedDeviceScaledDensity = scaledDensity;
        return this;
    }

    /**
     * This method is used to set current screen size.
     * Means device screen size that going to be optimized.
     *
     * @param width  Current device screen width.
     * @param height Current device screen height.
     * @return Builder's instance to make possible to add more config.
     */
    public DeviceBuilder currentScreen(int width, int height) {
        mCurrentDisplayWidth = width;
        mCurrentDisplayHeight = height;
        return this;
    }

    /**
     * This method is used to set tested screen size.
     * Means device screen size that user has tested ui for
     *
     * @param width  Tested device screen width.
     * @param height Tested device screen height.
     * @return Builder's instance to make possible to add more config.
     */
    public DeviceBuilder testedScreen(int width, int height) {
        mTestedDisplayWidth = width;
        mTestedDisplayHeight = height;
        return this;
    }

    /**
     * This method is used to make basic configuration before stare an optimization.
     *
     * @param context Context of the component where optimization going to be executed.
     */
    void configure(Context context) {
        if (mCurrentDisplayWidth == DEFAULT_SCREEN_VALUE ||
                mCurrentDisplayHeight == DEFAULT_SCREEN_VALUE) {
            if (!isNull(context)) {
                DisplayMetrics metrics = getCurrentDisplayMetrics(context);
                mCurrentDisplayWidth = metrics.widthPixels;
                mCurrentDisplayHeight = metrics.heightPixels;
            }
        }
        mCurrDeviceDensity = getDensity(context);
        mCurrDeviceScaledDensity = getScaledDensity(context);
    }

    /**
     * Provides a means to get current devices screen width.
     *
     * @return Display width.
     */
    int getCurrentDisplayWidth() {
        return mCurrentDisplayWidth;
    }

    /**
     * Provides a means to get current devices screen height.
     *
     * @return Display height.
     */
    int getCurrentDisplayHeight() {
        return mCurrentDisplayHeight;
    }

    /**
     * Provides a means to get tested devices screen width.
     *
     * @return Display width.
     */
    int getTestedDisplayWidth() {
        return mTestedDisplayWidth;
    }

    /**
     * Provides a means to get tested devices screen height.
     *
     * @return Display height.
     */
    int getTestedDisplayHeight() {
        return mTestedDisplayHeight;
    }

    /**
     * Provides a means to get tested devices screen density.
     *
     * @return Screen density.
     */
    float getTestedDeviceDensity() {
        return mTestedDeviceDensity;
    }

    /**
     * Provides a means to get tested devices screen scaled density.
     *
     * @return Screen scaled density.
     */
    float getTestedDeviceScaledDensity() {
        return mTestedDeviceScaledDensity;
    }

    /**
     * Provides a means to get current devices screen density.
     *
     * @return Screen density.
     */
    float getCurrentDeviceDensity() {
        return mCurrDeviceDensity;
    }

    /**
     * Provides a means to get current devices screen scaled density.
     *
     * @return Screen scaled density.
     */
    float getCurrentDeviceScaledDensity() {
        return mCurrDeviceScaledDensity;
    }
}