# meijue-ui


[![Download](https://api.bintray.com/packages/hedzr/maven/meijue-ui/images/download.svg)](https://bintray.com/hedzr/maven/meijue-ui/_latestVersion)
[![Release](https://jitpack.io/v/hedzr/meijue-ui.svg)](https://jitpack.io/#hedzr/meijue-ui)


`meijue-ui` is a kotlin extensions library to provide those knives (such as xxHelpers, xxUtil, xxTool)
in Android developing.



## Branches

### 1.1

lower than AS 3.2, without Android X.
 
### 1.3

for AS 3.2 or higher, with Android X.




## Guide

### 1. add dependency

```gradle
	implementation 'com.obsez.mobile.meijue.ui:meijue-ui:$meijue_ui_version'
```

#### or, jitpack

To get all available imports, lookup: https://jitpack.io/#hedzr/meijue-ui

```gradle
	implementation 'com.github.hedzr:meijue-ui:master-SNAPSHOT'
	// or:
	// implementation 'com.github.hedzr:meijue-ui:release~v1.1.6'
```

#### using `devel` branch

If you wanna retrieve the working branch with the latest fixes or patches, import the `devel` branch:

```gradle
    implementation 'com.github.hedzr:meijue-ui:devel-SNAPSHOT'
```



### 2. initialize meijue-ui module in your app:

```java
public class AnimApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        MeijueUiAppModule.get().init(this);
    }
}
```

### 3. use Base classes

when you need a simple base class and structure, `BaseActivity` or `ToolbarAnimActivity` would be one of choices.

Sample here:

```kotlin
class MainActivity : ToolbarAnimActivity(), NavigationView.OnNavigationItemSelectedListener {

    override val fabUi: FloatingActionButton? by lazy { fab }

    override val toolbarUi: Toolbar? by lazy { toolbar }

    override val navDrawerUi: NavigationView? by lazy { nav_view }

    override val drawerLayoutUi: DrawerLayout? by lazy{ drawer_layout }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setupFab() {
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun setupNavDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }
}
```

### 4. use kotlin extensions

once you integrated `meijue-ui` library, a set of extensions can be imported and used:

```kotlin
snackBar("i'm here") {
    action("fine") {
        toast("thanks")
    }
}
```

```kotlin
class MainActivity: AppCompatActivity() {
    fun doSomething(){
        startActivity<LoginActivity>()
    }
}
```

### using `HandsetPlugDetectReceiver`

1. declare the permissions in `AndroidManifest.xml`:
   ```xml
       <uses-permission android:name="android.permission.BLUETOOTH" />
   ```

2. declare the receiver in `AndroidManifest.xml`:
   ```xml
       <receiver
           android:name=".HandsetPlugDetectReceiver"
           android:enabled="true"
           android:exported="true" />
   ```

2. register `HandsetPlugDetectReceiver` in your MainActivity:

   ```kotlin
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)
           //setSupportActionBar(toolbar)
           checkHandsetStatus()
       }

       private fun checkHandsetStatus(){
           HandsetPlugDetectReceiver.register(this) {
               snackBar("headset present: $it (recheck: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}), bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}", Snackbar.LENGTH_INDEFINITE)
           }
           Timber.v("headset present: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}")
           Timber.v("bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}")
           snackBar("headset present: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}, bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}", Snackbar.LENGTH_INDEFINITE)
       }

       override fun onDestroy() {
           super.onDestroy()
           HandsetPlugDetectReceiver.unregister(this)
       }
   ```


## Documentations

#### 1. ui
#### 2. extensions

todo.

## LICENSE

MIT

```
Copyright 2015-2018 Hedzr Yeh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```

















