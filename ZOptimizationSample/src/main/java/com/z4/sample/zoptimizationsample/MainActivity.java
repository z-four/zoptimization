package com.z4.sample.zoptimizationsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.z4.zoptimization.ConfigBuilder;
import com.z4.zoptimization.ZOptimization;

import static android.graphics.Typeface.createFromAsset;
import static constants.Constants.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView());

        initUi();
    }

    private ViewGroup getProperContentView() {
        return ZOptimization.with(this)
                .layout(R.layout.activity_main)
                .testedDeviceDisplaySize(TESTED_DEVICE_DISPLAY_WIDTH, TESTED_DEVICE_DISPLAY_HEIGHT)
                .config(new ConfigBuilder()
                        .applyDp(TESTED_DEVICE_DENSITY)
                        .applySp(TESTED_DEVICE_SCALED_DENSITY))
                .makeOptimization();
    }

    protected void initUi() {
        ((TextView) findViewById(R.id.splash_text)).setTypeface(createFromAsset(getAssets(),
                PACIFICO_FONT));
    }
}