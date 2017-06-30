package com.z4.zoptimization;

import android.app.Service;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
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

import static com.z4.zoptimization.ZOptimization.Type.*;
import static com.z4.zoptimization.ZUtils.*;
import static com.z4.zoptimization.ZConst.*;
import static com.z4.zoptimization.ZConst.ViewParams.*;
import static com.z4.zoptimization.ZConst.DeviceType.*;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class ZOptimization {
    private static Context sContext;
    private LayoutInflater mLayoutInflater;
    private List<Integer> mExcludeIds;
    private Map<Short, List<Integer>> mConfigIds;
    private ViewGroup mViewGroup;

    private int mLayoutId;

    private int mCurrentDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mCurrentDisplayHeight = DEFAULT_SCREEN_VALUE;
    private int mDefaultDisplayWidth = DEFAULT_SCREEN_VALUE;
    private int mDefaultDisplayHeight = DEFAULT_SCREEN_VALUE;

    private int mTextSizeComplexUnit = COMPLEX_UNIT_PX;
    private int mViewSizeComplexUnit = COMPLEX_UNIT_PX;
    private float mDefaultDensity = 1.0f;
    private float mDefaultScaledDensity = 1.0f;
    private float mCurrentDensity = 1.0f;
    private float mCurrentScaledDensity = 1.0f;

    private short mDeviceType = ALL_DEVICES;
    private short mCurrDeviceType = PHONE;
    private boolean mPaddingEnabled = true;
    private boolean mMarginEnabled = true;
    private boolean mViewSizeEnabled = true;
    private boolean mTextSizeEnabled = true;

    public interface Type {
        short VIEW_OPTIMIZATION = 1;
        short TEXT_OPTIMIZATION = 2;
        short PADDING_OPTIMIZATION = 3;
        short MARGIN_OPTIMIZATION = 4;
    }

    private ZOptimization() {
        if (isNull(sContext)) return;
        mLayoutInflater = (LayoutInflater) sContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    public static void init(Context context) {
        sContext = context;
    }

    private void determinateDisplaySize() {
        if (isNull(sContext)) return;

        mCurrentDisplayWidth = getCurrentDisplayWidth(sContext);
        mCurrentDisplayHeight = getCurrentDisplayHeight(sContext);
    }

    private ViewGroup getViewGroup() {
        return isNull(mViewGroup) ? (ViewGroup) mLayoutInflater.inflate(mLayoutId, null)
                : mViewGroup;
    }

    private ViewGroup beginOptimization() {
        ViewGroup viewGroup = getViewGroup();
        mCurrDeviceType = isTablet(sContext) ? TABLET : PHONE;

        if (isNull(sContext) || !isProperDeviceType()) return viewGroup;

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
        if (!isOptimizationDisable(TEXT_OPTIMIZATION, view.getId())) {
            if (view instanceof TextView) {
                int textSize = getProperTextSize((int) ((TextView) view).getTextSize());
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }

    private void sizeOptimization(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (!isNull(layoutParams) && !isOptimizationDisable(VIEW_OPTIMIZATION, view.getId())) {
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
        if (!isOptimizationDisable(PADDING_OPTIMIZATION, view.getId())) {
            int properTopPadding = getProperParam(getProperMarginY(view.getPaddingTop()));
            int properBottomPadding = getProperParam(getProperMarginY(view.getPaddingBottom()));
            int properLeftPadding = getProperParam(getProperMarginX(view.getPaddingLeft()));
            int properRightPadding = getProperParam(getProperMarginX(view.getPaddingRight()));

            view.setPadding(properLeftPadding, properTopPadding, properRightPadding, properBottomPadding);
        }
    }

    private void marginOptimization(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        if (!isNull(params) && !isOptimizationDisable(MARGIN_OPTIMIZATION, view.getId())) {
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
        return (mDeviceType == ALL_DEVICES || mDeviceType == mCurrDeviceType);
    }

    private boolean isOptimizationDisable (short optimizationType, int id) {
        return !isNull(mConfigIds) && mConfigIds.containsKey(optimizationType)
                && mConfigIds.get(optimizationType).contains(id);
    }

    private boolean containsId(int id) {
        return !isNull(mExcludeIds) && mExcludeIds.contains(id);
    }

    private int getProperParam(int value) {
        if (!checkIfNotZero(value)) return value;
        value -= E;

        return mCurrDeviceType == TABLET ? value *= TABLET_COF : value;
    }

    public int getProperMarginY(int marginY) {
        if (mTextSizeComplexUnit != TypedValue.COMPLEX_UNIT_PX) {
            marginY = (int) ((marginY * mDefaultDensity) / mCurrentDensity);
        }
        return (mCurrentDisplayHeight * marginY) / mDefaultDisplayHeight;
    }

    public int getProperMarginX(int marginX) {
        if (mTextSizeComplexUnit != TypedValue.COMPLEX_UNIT_PX) {
            marginX = (int) ((marginX * mDefaultDensity) / mCurrentDensity);
        }
        return (mCurrentDisplayWidth * marginX) / mDefaultDisplayWidth;
    }

    public int getProperTextSize(int currentSizeInPx) {
        if (mTextSizeComplexUnit != TypedValue.COMPLEX_UNIT_PX) {
            currentSizeInPx = (int) ((currentSizeInPx * mDefaultScaledDensity) / mCurrentScaledDensity);
        }
        return (mCurrentDisplayHeight * currentSizeInPx) / mDefaultDisplayHeight;
    }

    private int getProperWidth(int currViewWidth) {
        if (mViewSizeComplexUnit != TypedValue.COMPLEX_UNIT_PX) {
            currViewWidth = (int) ((currViewWidth * mDefaultDensity) / mCurrentDensity);
        }
        return (mCurrentDisplayWidth * currViewWidth) / mDefaultDisplayWidth;
    }

    public int getProperWidth(int currViewWidth, int defDisplayWidth) {
        mDefaultDisplayWidth = defDisplayWidth;
        return getProperWidth(currViewWidth);
    }

    private int getProperHeight(int currViewHeight) {
        if (mViewSizeComplexUnit != TypedValue.COMPLEX_UNIT_PX) {
            currViewHeight = (int) ((currViewHeight * mDefaultDensity) / mCurrentDensity);
        }
        return (mCurrentDisplayHeight * currViewHeight) / mDefaultDisplayHeight;
    }

    public int getProperHeight(int currViewHeight, int defDisplayHeight) {
        mDefaultDisplayHeight = defDisplayHeight;
        return getProperHeight(currViewHeight);
    }

    public static ZOptimization get() {
        return new ZOptimization();
    }

    public static ZOptimizationBuilder withContext(Context context) {
        init(context);
        return get().new ZOptimizationBuilder();
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

        public ZOptimizationBuilder defaultDisplaySize(int width, int height) {
            ZOptimization.this.mDefaultDisplayWidth = width;
            ZOptimization.this.mDefaultDisplayHeight = height;
            return this;
        }

        public ZOptimizationBuilder currentDisplaySize(int width, int height) {
            ZOptimization.this.mCurrentDisplayWidth = width;
            ZOptimization.this.mCurrentDisplayHeight = height;
            return this;
        }

        public ZOptimizationBuilder measureConfiguration(int complexUnit, float defaultScreenDensity) {
            ZOptimization.this.mCurrentDensity = getDensity(sContext);
            ZOptimization.this.mDefaultDensity = defaultScreenDensity;
            ZOptimization.this.mViewSizeComplexUnit = complexUnit;
            return this;
        }

        public ZOptimizationBuilder textConfiguration(int complexUnit, float defaultScreenDensity) {
            ZOptimization.this.mTextSizeComplexUnit = complexUnit;
            ZOptimization.this.mDefaultScaledDensity = defaultScreenDensity;
            ZOptimization.this.mCurrentScaledDensity = getScaledDensity(sContext);
            return this;
        }

        public ZOptimizationBuilder determinateDisplaySize() {
            ZOptimization.this.determinateDisplaySize();
            return this;
        }

        public ZOptimizationBuilder deviceType(short type) {
            if (type >= PHONE && type <= ALL_DEVICES) ZOptimization.this.mDeviceType = type;
            return this;
        }

        public ViewGroup makeOptimization() {
            return beginOptimization();
        }
    }
}