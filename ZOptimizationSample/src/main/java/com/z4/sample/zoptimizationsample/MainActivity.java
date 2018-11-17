package com.z4.sample.zoptimizationsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.TimingLogger;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.z4.zoptimization.DeviceBuilder;
import com.z4.zoptimization.ZOptimization;

import static android.graphics.Typeface.createFromAsset;
import static com.z4.sample.zoptimizationsample.Constants.*;

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
                .onlyFor(ZOptimization.Device.PHONE)
                .exclude(ZOptimization.Type.ALL, R.id.login_button)
                .exclude(ZOptimization.Type.TEXT, AppCompatButton.class)
                .enable(false, true, true, true)
                .config(new DeviceBuilder()
                        .applyDp(TESTED_DEVICE_DENSITY)
                        .applySp(TESTED_DEVICE_SCALED_DENSITY)
                        .testedScreen(TESTED_DEVICE_DISPLAY_WIDTH, TESTED_DEVICE_DISPLAY_HEIGHT))
                .execute();
    }

    protected void initUi() {
        ((TextView) findViewById(R.id.splash_text)).setTypeface(createFromAsset(getAssets(),
                PACIFICO_FONT));
    }
}