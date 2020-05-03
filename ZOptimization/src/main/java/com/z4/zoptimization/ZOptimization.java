package com.z4.zoptimization;

import android.app.Service;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static com.z4.zoptimization.Utils.isEmpty;
import static com.z4.zoptimization.Utils.isNull;
import static com.z4.zoptimization.Utils.isTablet;
import static com.z4.zoptimization.ZOptimization.Device.PHONE;
import static com.z4.zoptimization.ZOptimization.Device.TABLET;
import static com.z4.zoptimization.ZOptimization.Type.MARGIN;
import static com.z4.zoptimization.ZOptimization.Type.PADDING;
import static com.z4.zoptimization.ZOptimization.Type.TEXT;
import static com.z4.zoptimization.ZOptimization.Type.VIEW;

public final class ZOptimization {
    private static final float TABLET_COF = 1.075f;
    private static final int E = 1;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Map<Type, List<Integer>> mConfigIds;
    private Map<Type, List<Class>> mConfigClasses;
    private ViewGroup mViewGroup;
    private DeviceBuilder mDeviceBuilder;

    private int mLayoutId;

    private boolean mPaddingEnabled = true;
    private boolean mMarginEnabled = true;
    private boolean mViewSizeEnabled = true;
    private boolean mTextSizeEnabled = true;

    private Device mDeviceType = Device.BOTH;
    private Device mCurrDeviceType = Device.PHONE;

    public enum Type {
        VIEW,
        TEXT,
        PADDING,
        MARGIN,
        ALL
    }

    public enum Device {
        TABLET,
        PHONE,
        BOTH
    }

    private ZOptimization() {}

    private ZOptimization(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    private ViewGroup getViewGroup() {
        return isNull(mViewGroup) ? (ViewGroup) mLayoutInflater.inflate(mLayoutId, null)
                : mViewGroup;
    }

    private ViewGroup beginOptimization() {
        final ViewGroup viewGroup = getViewGroup();

        mCurrDeviceType = !isNull(mContext) && isTablet(mContext) ? TABLET : PHONE;
        if (isNull(mContext, mDeviceBuilder) || !isProperDeviceType()) return viewGroup;

        return fullOptimization(viewGroup);
    }

    private ViewGroup fullOptimization(ViewGroup viewGroup) {
        makeOptimization(viewGroup);

        final int viewGroupChildCount = viewGroup.getChildCount();

        for (int i = 0; i < viewGroupChildCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) fullOptimization((ViewGroup) view);
            else makeOptimization(view);
        }
        return viewGroup;
    }

    private void makeOptimization(View view) {
        if (mViewSizeEnabled) sizeOptimization(view);
        if (mMarginEnabled) marginOptimization(view);
        if (mPaddingEnabled) paddingOptimization(view);
        if (mTextSizeEnabled) textSizeOptimization(view);
    }

    private void textSizeOptimization(View view) {
        if (!isOptimizationDisabled(TEXT, view)) {
            if (view instanceof TextView) {
                int textSize = getProperTextSize((int) ((TextView) view).getTextSize());
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }

    private void sizeOptimization(View view) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (!isNull(layoutParams) && !isOptimizationDisabled(VIEW, view)) {
            final int width = layoutParams.width;
            final int height = layoutParams.height;

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
        if (!isOptimizationDisabled(PADDING, view)) {
            int properTopPadding = getProperParam(getProperMarginY(view.getPaddingTop()));
            int properBottomPadding = getProperParam(getProperMarginY(view.getPaddingBottom()));
            int properLeftPadding = getProperParam(getProperMarginX(view.getPaddingLeft()));
            int properRightPadding = getProperParam(getProperMarginX(view.getPaddingRight()));

            view.setPadding(properLeftPadding, properTopPadding, properRightPadding, properBottomPadding);
        }
    }

    private void marginOptimization(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        if (!isNull(params) && !isOptimizationDisabled(MARGIN, view)) {
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
        return (mDeviceType == Device.BOTH || mDeviceType == mCurrDeviceType);
    }

    private boolean isOptimizationDisabled(Type type, View view) {
        boolean isContainsId = !isNull(mConfigIds) && mConfigIds.containsKey(type)
                && mConfigIds.get(type).contains(view.getId());
        boolean isContainsClass = !isNull(mConfigClasses) && mConfigClasses.containsKey(type)
                && mConfigClasses.get(type).contains(view.getClass());
        isContainsId = isContainsId || !isNull(mConfigIds) && mConfigIds.containsKey(Type.ALL)
                && mConfigIds.get(Type.ALL).contains(view.getId());
        isContainsClass = isContainsClass || !isNull(mConfigClasses) && mConfigClasses.containsKey(Type.ALL)
                && mConfigClasses.get(Type.ALL).contains(view.getClass());

        return isContainsId || isContainsClass;
    }

    private int getProperParam(int value) {
        if (isEmpty(value)) return value;
        value -= E;

        return mCurrDeviceType == TABLET ? value *= TABLET_COF : value;
    }

    public int getProperMarginY(int marginY) {
        if (!isNull(mDeviceBuilder) && mDeviceBuilder.getTestedDeviceDensity() != -1) {
            marginY = (int) ((marginY * mDeviceBuilder.getTestedDeviceDensity())
                    / mDeviceBuilder.getCurrentDeviceDensity());
        }
        return (mDeviceBuilder.getCurrentDisplayHeight() * marginY) / mDeviceBuilder.getTestedDisplayHeight();
    }

    public int getProperMarginX(int marginX) {
        if (!isNull(mDeviceBuilder) && mDeviceBuilder.getTestedDeviceDensity() != -1) {
            marginX = (int) ((marginX * mDeviceBuilder.getTestedDeviceDensity())
                    / mDeviceBuilder.getCurrentDeviceDensity());
        }
        return (mDeviceBuilder.getCurrentDisplayWidth() * marginX) / mDeviceBuilder.getTestedDisplayWidth();
    }

    public int getProperTextSize(int currSizeInPx) {
        if (!isNull(mDeviceBuilder) && mDeviceBuilder.getTestedDeviceScaledDensity() != -1) {
            currSizeInPx = (int) ((currSizeInPx * mDeviceBuilder.getTestedDeviceScaledDensity())
                    / mDeviceBuilder.getCurrentDeviceScaledDensity());
        }
        return (mDeviceBuilder.getCurrentDisplayHeight() * currSizeInPx) / mDeviceBuilder.getTestedDisplayHeight();
    }

    public int getProperWidth(int currViewWidth) {
        if (!isNull(mDeviceBuilder) && mDeviceBuilder.getTestedDeviceDensity() != -1) {
            currViewWidth = (int) ((currViewWidth * mDeviceBuilder.getTestedDeviceDensity())
                    / mDeviceBuilder.getCurrentDeviceDensity());
        }
        return (mDeviceBuilder.getCurrentDisplayWidth() * currViewWidth) / mDeviceBuilder.getTestedDisplayWidth();
    }

    public int getProperHeight(int currViewHeight) {
        if (!isNull(mDeviceBuilder) && mDeviceBuilder.getTestedDeviceDensity() != -1) {
            currViewHeight = (int) ((currViewHeight * mDeviceBuilder.getTestedDeviceDensity())
                    / mDeviceBuilder.getCurrentDeviceDensity());
        }

        return (mDeviceBuilder.getCurrentDisplayHeight() * currViewHeight) / mDeviceBuilder.getTestedDisplayHeight();
    }

    public static ZOptimization init() { return new ZOptimization(); }

    public static ZOptimizationBuilder with(Context context) {
        return new ZOptimization(context).new ZOptimizationBuilder();
    }

    //Builder
    public class ZOptimizationBuilder {

        private ZOptimizationBuilder() {}

        public ZOptimizationBuilder enable(boolean padding, boolean margin, boolean viewSize,
                                           boolean textSize) {
            ZOptimization.this.mPaddingEnabled = padding;
            ZOptimization.this.mMarginEnabled = margin;
            ZOptimization.this.mViewSizeEnabled = viewSize;
            ZOptimization.this.mTextSizeEnabled = textSize;
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

        public ZOptimizationBuilder exclude(Type type, Class... classes) {
            if (isNull(ZOptimization.this.mConfigClasses)) {
                ZOptimization.this.mConfigClasses = new HashMap<>();
            }
            ZOptimization.this.mConfigClasses.put(type, Arrays.asList(classes));

            return this;
        }

        public ZOptimizationBuilder exclude(Type type, Integer... ids) {
            if (isNull(ZOptimization.this.mConfigIds)) {
                ZOptimization.this.mConfigIds = new HashMap<>();
            }
            ZOptimization.this.mConfigIds.put(type, Arrays.asList(ids));

            return this;
        }

        public ZOptimizationBuilder onlyFor(Device type) {
            mDeviceType = type;
            return this;
        }

        public ZOptimizationBuilder config(DeviceBuilder deviceBuilder) {
            mDeviceBuilder = deviceBuilder;
            if (!isNull(mDeviceBuilder)) mDeviceBuilder.configure(mContext);
            return this;
        }

        public ViewGroup execute() { return beginOptimization(); }
    }
}