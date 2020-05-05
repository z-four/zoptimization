package com.z4.sample.zoptimizationsample;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.z4.zoptimization.DeviceBuilder;
import com.z4.zoptimization.ZOptimization;

import static android.graphics.Typeface.createFromAsset;
import static com.z4.sample.zoptimizationsample.Constants.PACIFICO_FONT;
import static com.z4.sample.zoptimizationsample.Constants.TESTED_DEVICE_DENSITY;
import static com.z4.sample.zoptimizationsample.Constants.TESTED_DEVICE_DISPLAY_HEIGHT;
import static com.z4.sample.zoptimizationsample.Constants.TESTED_DEVICE_DISPLAY_WIDTH;
import static com.z4.sample.zoptimizationsample.Constants.TESTED_DEVICE_SCALED_DENSITY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView());

        setUpView();
    }

    private void setUpView() {
        ((TextView) findViewById(R.id.splash_text)).setTypeface(createFromAsset(getAssets(),
                PACIFICO_FONT));
    }

    private ViewGroup getProperContentView() {
        return ZOptimization.with(this)
                .layout(R.layout.activity_main)
                .onlyFor(ZOptimization.Device.PHONE)
                .exclude(ZOptimization.Type.ALL, R.id.login_button)
                .enable(false, true, true, true)
                .config(new DeviceBuilder()
                        .applyDp(TESTED_DEVICE_DENSITY)
                        .applySp(TESTED_DEVICE_SCALED_DENSITY)
                        .testedScreen(TESTED_DEVICE_DISPLAY_WIDTH, TESTED_DEVICE_DISPLAY_HEIGHT))
                .execute();
    }
}