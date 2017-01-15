package com.z4.zoptimization;

public interface ZConst {

    String LOG_TAG = "optimization";
    float TABLET_COF = 1.075f;
    int E = 1;
    int DEFAULT_SCREEN_VALUE = 1;

    interface ViewParams {
        short MATCH_PARENT = -1;
        short WRAP_CONTENT = -2;
    }

    interface DeviceType {
        short PHONE = 1;
        short TABLET = 2;
        short ALL_DEVICES = 3;
    }
}
