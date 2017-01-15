package com.z4.example.zoptimization;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.z4.zoptimization.ZOptimization;

public class MainActivity extends AppCompatActivity {
    private final static int DEFAULT_DISPLAY_HEIGHT = 1280;
    private final static int DEFAULT_DISPLAY_WIDTH  = 768;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView(R.layout.activity_main));
    }

    protected View getProperContentView(@LayoutRes int layoutResId) {
        return ZOptimization.withContext(this)
                .resourceId(layoutResId)
                .defaultDisplaySize(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT)
                .determinateDisplaySize()
                .makeOptimization();
    }
}
