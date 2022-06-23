About
--------

A way to optimize the user interface without having configuration qualifiers for different pixel densities.

Example
--------

![optimization](/images/image.png)

Usage
--------

#### Create layout for a specific screen.

```xml
 <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <Button
        android:layout_marginLeft="10dp"
        android:layout_width="300dp"
        android:layout_height="200dp" />
 
    <TextView
        android:textSize="17sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
 
    <View
        android:padding="20dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
    ...
  </RelativeLayout>
```

#### Activity example

```java
public class MainActivity extends Activity {
    private final static int TESTED_DEVICE_DISPLAY_HEIGHT = 1280;
    private final static int TESTED_DEVICE_DISPLAY_WIDTH = 768;
    
    private final static float TESTED_DEVICE_SCALED_DENSITY = 3.0f;
    private final static float TESTED_DEVICE_DENSITY = 3.0f;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getProperContentView());
        
        // How to get display metrics for the tested device
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density; //TESTED_DEVICE_DENSITY
        float scaledDensity = metrics.scaledDensity; //TESTED_DEVICE_SCALED_DENSITY
        float width = metrics.widthPixels; //TESTED_DEVICE_DISPLAY_WIDTH
        float height = metrics.heightPixels; //TESTED_DEVICE_DISPLAY_HEIGHT
        ...
    }
    
    private ViewGroup getProperContentView() {
        return ZOptimization.with(this)
                .layout(R.layout.activity_main)
                .onlyFor(ZOptimization.Device.PHONE) // Optimized for phone only, also TABLET or BOTH can be used.
                .exclude(ZOptimization.Type.ALL, R.id.login_button) // Disable optimization for specific ids.
                .exclude(ZOptimization.Type.TEXT, AppCompatButton.class) // Disable text size optimization for specific classes.
                .enable(false, true, true, true) // Enable/disable optimization types
                .config(new DeviceBuilder()
                        .applyDp(TESTED_DEVICE_DENSITY)
                        .applySp(TESTED_DEVICE_SCALED_DENSITY)
                        .testedScreen(TESTED_DEVICE_DISPLAY_WIDTH, TESTED_DEVICE_DISPLAY_HEIGHT))
                .execute();
    }
}
```

#### RecyclerView example
```java
@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
   ViewGroup itemView = (ViewGroup)  LayoutInflater.from(parent.getContext())
           .inflate(R.layout.item_layout, parent, false);
 
   return new MyViewHolder(getProperContentView(itemView));
}
    
private ViewGroup getProperContentView(ViewGroup viewGroup) {
    return ZOptimization.with(viewGroup.getContext())
            .layout(viewGroup)
            .config(new DeviceBuilder()
                    .applyDp(TESTED_DEVICE_DENSITY)
                    .applySp(TESTED_DEVICE_SCALED_DENSITY)
                    .testedScreen(TESTED_DEVICE_DISPLAY_WIDTH, TESTED_DEVICE_DISPLAY_HEIGHT))
            .execute();
}
```

#### Note: If the config is not specified, then COMPLEX_UNIT_PX will be used by default

Download
--------

```groovy
dependencies {
  compile 'com.github.z-four:zoptimization:1.0.5'
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
|onlyFor(enum)| Set device type (default: BOTH)|
|testedScreen(int, int)| Set tested display size (width, height)|
|currentScreen(int, int)| Set current display size (width, height)|
|exclude(enum, int[])| Setup optimization type & specific ids should be excluded|
|exclude(enum, class[])| Setup optimization type & specific classes should be excluded|
|applySp(float)| Setup tested device scaled density|
|applyDp(float)| Setup tested device density|
|enable(boolean, boolean, boolean, boolean)| Enable/disable optimization for all the types|
|execute()| Start optimization|

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
