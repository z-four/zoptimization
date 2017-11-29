ZOptimization
============

What is ZOptimization
--------

Android library for optimization your UI. 
All you have to do is create an xml file for a specific screen. 
The rest is done by the library.

Example
--------

![optimization](/images/image.png)

Usage
--------

#### Create an xml file for a specific screen.

```xml
 <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <Button
        android:layout_marginLeft="10dp"
        android:layout_width="300dp"
        android:layout_height="200dp" />
    ...
  </RelativeLayout>
```

#### Set display size which used by default.

```java
public class MainActivity extends Activity {
    private final static int TESTED_DEVICE_DISPLAY_HEIGHT = 1280;
    private final static int TESTED_DEVICET_DISPLAY_WIDTH = 768;
    
    private final static float TESTED_DEVICET_SCALED_DENSITY = 3.0f;
    private final static float TESTED_DEVICET_DENSITY = 3.0f;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView());
        
        //How to get density & scaledDensity for tested device
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density; //DEFAULT_DENSITY
        float scaledDensity = metrics.scaledDensity; //DEFAULT_SCALED_DENSITY
        ...
    }

    protected ViewGroup getProperContentView() {
        return ZOptimization.with(this)
                .layout(R.layout.activity_main)
                .excludeIds(R.id.divider, R.id.login_button) //Disable optimization for specific ids.
                .deviceType(ZOptimization.DeviceType.TABLET) //Optimized for tablets only.
                .testedDeviceDisplaySize(TESTED_DEVICE_DISPLAY_WIDTH, TESTED_DEVICE_DISPLAY_HEIGHT) 
                .config(new ConfigBuilder()
                        .applyDp(TESTED_DEVICE_DENSITY)
                        .applySp(TESTED_DEVICE_SCALED_DENSITY))
                .makeOptimization();
    }
}
```
#### Note: If the config is not specified, then COMPLEX_UNIT_PX will be used by default

Download
--------

```groovy
dependencies {
  compile 'com.github.z-four:zoptimization:1.0.3'
}
```

```groovy
allprojects {
    repositories {
        ...
        maven {
            url "https://jitpack.io"
        }
    }
}
```

Methods
--------

| Name | Description |
|-------|------------|
|layout(int)| Set layout id which need to optimize|
|layout(ViewGroup)| Set ViewGroup which need to optimize|
|deviceType(short)| Set device type (default: ALL_DEVICES)|
|testedDeviceDisplaySize(int, int)| Set default display size (width, height)|
|currentDisplaySize(int, int)| Set current display size (width, height)|
|excludeIds(int[])| Disable optimization for specific ids|
|config(ConfigBuilder)| Setup tested device density & scaledDensity|
|disableOptimization(short, int[])| Setup optimization type (ZOptimization.Type) & specific ids for disable|
|marginEnable(boolean)| Enable or disable margin optimization (default: true)|
|paddingEnable(boolean)| Enable or disable padding optimization (default: true)|
|textSizeEnable(boolean)| Enable or disable text size optimization (default: true)|
|viewSizeEnable(boolean)| Enable or disable view size optimization (default: true)|

License
-------

    Copyright 2017 Dmitriy Zhyzhko

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
