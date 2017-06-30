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
    private final static int DEFAULT_DISPLAY_HEIGHT = 1280;
    private final static int DEFAULT_DISPLAY_WIDTH  = 768;
    private final static float DEFAULT_SCALED_DENSITY = 3.0f;
    private final static float DEFAULT_DENSITY  = 3.0f;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView());
        
        //How to get density & scaledDensity for tested device
        float density = getResources().getDisplayMetrics().density; // = DEFAULT_DENSITY
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity; // = DEFAULT_SCALED_DENSITY
        ...
    }

    protected ViewGroup getProperContentView() {
        return ZOptimization.withContext(this)
                .layout(R.layout.activity_main)
                .excludeIds(R.id.divider, R.id.login_button) //Disable optimization for specific ids.
                .deviceType(ZConst.DeviceType.TABLET) //Optimized for tablets only.
                .defaultDisplaySize(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT) //Tested device display width & height
                .measureConfiguration(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DENSITY) //Setup dp optimization.
                .textConfiguration(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SCALED_DENSITY) //Setup sp optimization.
                .determinateDisplaySize()
                .makeOptimization();
    }
}
```
#### Note: measureConfiguration and textConfiguration by default = TypedValue.COMPLEX_UNIT_PX.

Download
--------

```groovy
dependencies {
  compile 'com.github.z-four:zoptimization:1.0.1'
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
|defaultDisplaySize(int, int)| Set default display size (width, height)|
|currentDisplaySize(int, int)| Set current display size (width, height)|
|determinateDisplaySize()| Auto-determine current display size|
|excludeIds(int[])| Disable optimization for specific ids|
|measureConfiguration(short, int[])| Setup complexUnit & tested device density|
|textConfiguration(short, int[])| Setup complexUnit & tested device scaledDensity|
|disableOptimization(short, int[])| Setup optimization type (ZOptimization.Type) & specific ids for disable|
|marginEnable(boolean)| Enable or disable margin optimization (default: true)|
|paddingEnable(boolean)| Enable or disable padding optimization (default: true)|
|textSizeEnable(boolean)| Enable or disable text size optimization (default: true)|
|viewSizeEnable(boolean)| Enable or disable view size optimization (default: true)|

License
-------

    Copyright 2013 Dmitriy Zhyzhko

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
