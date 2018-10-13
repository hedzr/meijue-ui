# meijue-ui  [![Download](https://api.bintray.com/packages/hedzr/maven/meijue-ui/images/download.svg)](https://bintray.com/hedzr/maven/meijue-ui/_latestVersion)  [![Release](https://jitpack.io/v/hedzr/meijue-ui.svg)](https://jitpack.io/#hedzr/meijue-ui)  [![CircleCI](https://circleci.com/gh/hedzr/meijue-ui.svg?style=shield)](https://circleci.com/gh/hedzr/meijue-ui)


`meijue-ui` is a kotlin extensions library to provide those knives (such as xxHelpers, xxUtil, xxTool)
in Android developing.



## Branches

### 1.1

lower than AS 3.2, without Android X.

it's no longer updates on this branch.

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

> since 1.3.1, `BaseActivity` or `ToolbarAnimActivity` have been moved into child package `activity`.

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

#### 3.1. Enable Android native Night Mode:

in your `MainActivity`:

```kotlin
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //val pref = PreferenceUtil.get(this)
        //@AppCompatDelegate.NightMode val nightMode = pref.getInt("nightMode", AppCompatDelegate.getDefaultNightMode())
        //Timber.d("load nightMode : $nightMode")
        when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> menu.findItem(R.id.menu_night_mode_system)?.isChecked = true
            // 22:00 - 07:00 时间段内自动切换为夜间模式
            // 可以有机会拦截这个判定，改为实现自己的机制，例如依据日出日落（需要地理坐标）来变更。
            AppCompatDelegate.MODE_NIGHT_AUTO -> menu.findItem(R.id.menu_night_mode_auto)?.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> menu.findItem(R.id.menu_night_mode_night)?.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> menu.findItem(R.id.menu_night_mode_day)?.isChecked = true
        }
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_night_mode_system -> { nightMode = (AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); true }
            R.id.menu_night_mode_day -> { nightMode = (AppCompatDelegate.MODE_NIGHT_NO); true }
            R.id.menu_night_mode_night -> { nightMode = (AppCompatDelegate.MODE_NIGHT_YES); true }
            R.id.menu_night_mode_auto -> { nightMode = (AppCompatDelegate.MODE_NIGHT_AUTO); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
```

check out the completed codes and resources in demo app.

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

### 5. `HandsetPlugDetectReceiver`

`HandsetPlugDetectReceiver` can detect plug in or out event of Handset.

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



### 6. `NotificationUtil`

It is an utilitiy class to simplify the operation of Android Notification Center.

Usages:

a. simple

```kotlin
btn_notify_2.setOnClickListener {
    val idChannel = "channel_2"
    val description = "456"

    val idNotification = 1
    val contentN = "who am i 2"
    val descN = "this is a details 2"

    val intent1 = Intent(this, LoginActivity::class.java)
    val pi = PendingIntent.getActivity(this, 0, intent1, 0);

    // see also `2. initialize meijue-ui module`,
    // `MeijueUiAppModule.get().init(this)` must be invoked.
    NotificationUtil.instance
        .channel(idChannel, description)
        .builder(idNotification, contentN, descN) {
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_home_black_24dp))

            // auto cancel the notification with contentIntent
            setAutoCancel(true)
            setContentIntent(pi)
        }
        .show()
}
```

b. grouped

```kotlin
btn_notify_2.setOnClickListener {
    val idChannel = "channel_2"
    val description = "456"

    val idNotification = 1
    val contentN = "who am i 2"
    val descN = "this is a details 2"

    val intent1 = Intent(this, LoginActivity::class.java)
    val pi = PendingIntent.getActivity(this, 0, intent1, 0);

    // see also `2. initialize meijue-ui module`,
    // `MeijueUiAppModule.get().init(this)` must be invoked.
    val jackie = NotificationUtil.instance.defaultSenderBuilder
    	.setBot(true)
    	.build()
    val jasper = NotificationUtil.instance.defaultSenderBuilder
        .setName("Jasper Brown")
        .setIcon(IconCompat.createWithResource(this, R.drawable.ic_dashboard_black_24dp))
        .setBot(false).build()


    // see also `2. initialize meijue-ui module`,
    // `MeijueUiAppModule.get().init(this)` must be invoked.
    NotificationUtil.instance
        .channel(idChannel, description)
        .builder(idNotification, contentN, descN) {
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_home_black_24dp))

            // auto cancel the notification with contentIntent
            setAutoCancel(true)
            setContentIntent(pi)
        }
        .sender {
            setBot(false)
        }
        .messagingStyle {
            addMessage("Check this out!", Date().time - 600000, jackie)
            addMessage("r u sure?", Date().time - 180000, jackie)
            addMessage("okay, got it.", Date().time - 60000, jasper)
            addMessage("sounds good", Date().time, jackie)
            setGroupConversation(true)
            setConversationTitle("djsljdk®")
        }
        .action(R.drawable.ic_sync_black_24dp, "Process them", pi,
            NotificationCompat.Action.SEMANTIC_ACTION_DELETE)
        .show()
}
```

For the completed codes, see also the demo app.



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

















