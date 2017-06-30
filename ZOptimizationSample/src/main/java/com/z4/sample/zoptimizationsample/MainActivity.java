package com.z4.sample.zoptimizationsample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.z4.zoptimization.ZOptimization;
import com.z4.zoptimization.ZUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.z4.zoptimization.ZConst.LOG_TAG;
import static constants.Constants.*;

public class MainActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @BindView(R.id.splash_text) TextView appNameText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView());

        bindViews();
        initUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindViews();
    }

    protected ViewGroup getProperContentView() {
        return ZOptimization.withContext(this)
                .layout(R.layout.activity_main)
                .excludeIds(R.id.splash_text)
                .disableOptimization(ZOptimization.Type.MARGIN_OPTIMIZATION, R.id.password_edittext_image)
                .disableOptimization(ZOptimization.Type.PADDING_OPTIMIZATION, R.id.show_password_button)
                .disableOptimization(ZOptimization.Type.VIEW_OPTIMIZATION, R.id.login_button, R.id.show_password_button)
                .defaultDisplaySize(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT)
                .measureConfiguration(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DENSITY)
                .textConfiguration(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SCALED_DENSITY)
                .determinateDisplaySize()
                .makeOptimization();
    }

    protected void initUi() {
        Typeface font = Typeface.createFromAsset(getAssets(), PACIFICO_FONT);
        appNameText.setTypeface(font);
    }

    private void bindViews () {
        mUnbinder = ButterKnife.bind(this);
    }

    private void unbindViews () {
        if(mUnbinder != null) mUnbinder.unbind();
    }
}