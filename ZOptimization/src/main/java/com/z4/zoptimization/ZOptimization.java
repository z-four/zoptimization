package com.z4.zoptimization;

import android.app.Service;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static com.z4.zoptimization.ZOptimization.DeviceType.ALL;
import static com.z4.zoptimization.ZOptimization.DeviceType.PHONE;
import static com.z4.zoptimization.ZOptimization.DeviceType.TABLET;
import static com.z4.zoptimization.ZOptimization.OptimizationType.*;
import static com.z4.zoptimization.Utils.*;

public class ZOptimization {
    private static final float TABLET_COF = 1.075f;
    private static final int E = 1;
    private static final int DEFAULT_SCREEN_VALUE = 1;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Integer> mExcludeIds;
    private Map<Short, List<Integer>> mConfigIds;
    private ViewGroup mViewGroup;
    private ConfigBuilder mConfigBuilder;

    private int mLayoutId;

    private int mCurrentDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mCurrentDisplayHeight = DEFAULT_SCREEN_VALUE;
    private int mDefaultDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mDefaultDisplayHeight = DEFAULT_SCREEN_VALUE;

    private short mDeviceType = ALL;
    private short mCurrDeviceType = PHONE;
    private boolean mPaddingEnabled = true;
    private boolean mMarginEnabled = true;
    private boolean mViewSizeEnabled = true;
    private boolean mTextSizeEnabled = true;

    public interface OptimizationType {
        short VIEW = 1;
        short TEXT = 2;
        short PADDING = 3;
        short MARGIN = 4;
    }

    public interface DeviceType {
        short PHONE = 1;
        short TABLET = 2;
        short ALL = 3;
    }

    private ZOptimization(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    private void determinateDisplaySize() {
        if (isNull(mContext)) return;

        DisplayMetrics metrics = getCurrentDisplayMetrics(mContext);
        mCurrentDisplayWidth = metrics.widthPixels;
        mCurrentDisplayHeight = metrics.heightPixels;
    }

    private ViewGroup getViewGroup() {
        return isNull(mViewGroup) ? (ViewGroup) mLayoutInflater.inflate(mLayoutId, null)
                : mViewGroup;
    }

    private ViewGroup beginOptimization() {
        ViewGroup viewGroup = getViewGroup();
        mCurrDeviceType = isTablet(mContext) ? TABLET : PHONE;

        if (isNull(mContext) || !isProperDeviceType()) return viewGroup;

        return fullOptimization(viewGroup);
    }

    private ViewGroup fullOptimization(ViewGroup viewGroup) {
        makeOptimization(viewGroup);

        int viewGroupChildCount = viewGroup.getChildCount();

        for (int i = 0; i < viewGroupChildCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) fullOptimization((ViewGroup) view);
            else makeOptimization(view);
        }
        return viewGroup;
    }

    private void makeOptimization(View view) {
        if (!containsId(view.getId())) {
            if (mViewSizeEnabled) sizeOptimization(view);
            if (mMarginEnabled) marginOptimization(view);
            if (mPaddingEnabled) paddingOptimization(view);
            if (mTextSizeEnabled) textSizeOptimization(view);
        }
    }

    private void textSizeOptimization(View view) {
        if (!isOptimizationDisable(TEXT, view.getId())) {
            if (view instanceof TextView) {
                int textSize = getProperTextSize((int) ((TextView) view).getTextSize());
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }

    private void sizeOptimization(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (!isNull(layoutParams) && !isOptimizationDisable(VIEW, view.getId())) {
            int width = layoutParams.width;
            int height = layoutParams.height;

            int properWidth = width != WRAP_CONTENT && width != MATCH_PARENT ?
                    getProperWidth(width) : width;
            int properHeight = height != WRAP_CONTENT && height != MATCH_PARENT ?
                    getProperHeight(height) : height;

            properHeight = getProperParam(properHeight == 0 ? ++properHeight : properHeight);
            properWidth = width == height ? properHeight :
                    getProperParam(properWidth == 0 ? ++properWidth : properWidth);

            layoutParams.width = properWidth;
            layoutParams.height = properHeight;

            view.setLayoutParams(layoutParams);
        }
    }

    private void paddingOptimization(View view) {
        if (!isOptimizationDisable(PADDING, view.getId())) {
            int properTopPadding = getProperParam(getProperMarginY(view.getPaddingTop()));
            int properBottomPadding = getProperParam(getProperMarginY(view.getPaddingBottom()));
            int properLeftPadding = getProperParam(getProperMarginX(view.getPaddingLeft()));
            int properRightPadding = getProperParam(getProperMarginX(view.getPaddingRight()));

            view.setPadding(properLeftPadding, properTopPadding, properRightPadding, properBottomPadding);
        }
    }

    private void marginOptimization(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        if (!isNull(params) && !isOptimizationDisable(MARGIN, view.getId())) {
            int marginTop = getProperParam(getProperMarginY(params.topMargin));
            int marginBottom = getProperParam(getProperMarginY(params.bottomMargin));
            int marginLeft = getProperParam(getProperMarginX(params.leftMargin));
            int marginRight = getProperParam(getProperMarginX(params.rightMargin));

            params.topMargin = marginTop;
            params.bottomMargin = marginBottom;
            params.leftMargin = marginLeft;
            params.rightMargin = marginRight;

            view.setLayoutParams(params);
        }
    }

    private boolean isProperDeviceType() {
        return (mDeviceType == ALL || mDeviceType == mCurrDeviceType);
    }

    private boolean isOptimizationDisable (short optimizationType, int id) {
        return !isNull(mConfigIds) && mConfigIds.containsKey(optimizationType)
                && mConfigIds.get(optimizationType).contains(id);
    }

    private boolean containsId(int id) {
        return !isNull(mExcludeIds) && mExcludeIds.contains(id);
    }

    private int getProperParam(int value) {
        if (isEmpty(value)) return value;
        value -= E;

        return mCurrDeviceType == TABLET ? value *= TABLET_COF : value;
    }

    public int getProperMarginY(int marginY) {
        if (!isNull(mConfigBuilder) && mConfigBuilder.getTestedDeviceDensity() != -1) {
            marginY = (int) ((marginY * mConfigBuilder.getTestedDeviceDensity())
                    / mConfigBuilder.getCurrDeviceDensity());
        }
        return (mCurrentDisplayHeight * marginY) / mDefaultDisplayHeight;
    }

    public int getProperMarginX(int marginX) {
        if (!isNull(mConfigBuilder) && mConfigBuilder.getTestedDeviceDensity() != -1) {
            marginX = (int) ((marginX * mConfigBuilder.getTestedDeviceDensity())
                    / mConfigBuilder.getCurrDeviceDensity());
        }
        return (mCurrentDisplayWidth * marginX) / mDefaultDisplayWidth;
    }

    public int getProperTextSize(int currentSizeInPx) {
        if (!isNull(mConfigBuilder) && mConfigBuilder.getTestedDeviceScaledDensity() != -1) {
            currentSizeInPx = (int) ((currentSizeInPx * mConfigBuilder.getTestedDeviceScaledDensity())
                    / mConfigBuilder.getCurrDeviceScaledDensity());
        }
        return (mCurrentDisplayHeight * currentSizeInPx) / mDefaultDisplayHeight;
    }

    private int getProperWidth(int currViewWidth) {
        if (!isNull(mConfigBuilder) && mConfigBuilder.getTestedDeviceDensity() != -1) {
            currViewWidth = (int) ((currViewWidth * mConfigBuilder.getTestedDeviceDensity())
                    / mConfigBuilder.getCurrDeviceDensity());
        }
        return (mCurrentDisplayWidth * currViewWidth) / mDefaultDisplayWidth;
    }

    public int getProperWidth(int currViewWidth, int defDisplayWidth) {
        mDefaultDisplayWidth = defDisplayWidth;
        return getProperWidth(currViewWidth);
    }

    private int getProperHeight(int currViewHeight) {
        if (!isNull(mConfigBuilder) && mConfigBuilder.getTestedDeviceDensity() != -1) {
            currViewHeight = (int) ((currViewHeight * mConfigBuilder.getTestedDeviceDensity())
                    / mConfigBuilder.getCurrDeviceDensity());
        }
        return (mCurrentDisplayHeight * currViewHeight) / mDefaultDisplayHeight;
    }

    public int getProperHeight(int currViewHeight, int defDisplayHeight) {
        mDefaultDisplayHeight = defDisplayHeight;
        return getProperHeight(currViewHeight);
    }

    public static ZOptimization get(Context context) {
        return new ZOptimization(context);
    }

    public static ZOptimizationBuilder with(Context context) {
        return get(context).new ZOptimizationBuilder();
    }

    //Builder
    public class ZOptimizationBuilder {

        private ZOptimizationBuilder() {}

        public ZOptimizationBuilder paddingEnable(boolean enable) {
            ZOptimization.this.mPaddingEnabled = enable;
            return this;
        }

        public ZOptimizationBuilder viewSizeEnable(boolean enable) {
            ZOptimization.this.mViewSizeEnabled = enable;
            return this;
        }

        public ZOptimizationBuilder marginEnable(boolean enable) {
            ZOptimization.this.mMarginEnabled = enable;
            return this;
        }

        public ZOptimizationBuilder textSizeEnable(boolean enable) {
            ZOptimization.this.mTextSizeEnabled = enable;
            return this;
        }

        public ZOptimizationBuilder layout(@LayoutRes int layoutId) {
            ZOptimization.this.mLayoutId = layoutId;
            return this;
        }

        public ZOptimizationBuilder layout(ViewGroup viewGroup) {
            ZOptimization.this.mViewGroup = viewGroup;
            return this;
        }

        public ZOptimizationBuilder disableOptimization(short optimizationType, Integer... ids) {
            if (isNull(ZOptimization.this.mConfigIds)) ZOptimization.this.mConfigIds = new HashMap<>();

            ZOptimization.this.mConfigIds.put(optimizationType, Arrays.asList(ids));

            return this;
        }

        public ZOptimizationBuilder excludeIds(Integer... ids) {
            ZOptimization.this.mExcludeIds = new ArrayList<>(Arrays.asList(ids));
            return this;
        }

        public ZOptimizationBuilder testedDeviceDisplaySize(int width, int height) {
            ZOptimization.this.mDefaultDisplayWidth = width;
            ZOptimization.this.mDefaultDisplayHeight = height;
            return this;
        }

        public ZOptimizationBuilder currentDisplaySize(int width, int height) {
            ZOptimization.this.mCurrentDisplayWidth = width;
            ZOptimization.this.mCurrentDisplayHeight = height;
            return this;
        }

        public ZOptimizationBuilder config(ConfigBuilder configBuilder) {
            mConfigBuilder = configBuilder;
            if (!isNull(configBuilder)) configBuilder.configure(mContext);
            return this;
        }

        public ZOptimizationBuilder deviceType(short type) {
            if (type >= PHONE && type <= ALL) ZOptimization.this.mDeviceType = type;
            return this;
        }

        public ViewGroup makeOptimization() {
            ZOptimization.this.determinateDisplaySize();
            return beginOptimization();
        }
    }
}