# app for meijue-ui library



## 包含特性


1. Toolbar Animation


## DrawerLayout 调整

要去掉侧滑抽屉的全屏高度，将其放置于工具栏之下，需要修改xml。

修改之后的 `activity_main.xml` 形如：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:id="@+id/sample_main_layout"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:fitsSystemWindows="true"
              android:orientation="vertical">

    <!--<android.support.v7.widget.Toolbar-->
    <!--android:id="@+id/toolbar"-->
    <!--android:layout_width="match_parent"-->
    <!--android:layout_height="?attr/actionBarSize"-->
    <!--android:background="?attr/colorPrimary"-->
    <!--app:theme="@style/ThemeOverlay.AppCompat.Dark" />-->

    <android.support.design.widget.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.AppBarOverlay">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?attr/colorPrimary"
            app:popupTheme="@style/AppTheme.PopupOverlay"/>

    </android.support.design.widget.AppBarLayout>


<android.support.v4.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:openDrawer="start">

    <!--<include-->
        <!--layout="@layout/app_bar_main"-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="match_parent"/>-->

    <include layout="@layout/content_main"/>

    <android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@android:drawable/ic_dialog_email"/>

    <!--<FrameLayout-->
    <!--android:id="@+id/container"-->
    <!--android:layout_width="match_parent"-->
    <!--android:layout_height="match_parent" >-->

    <!--<TextView-->
    <!--android:padding="16dp"-->
    <!--android:text="NavigationView在Toolbar下方"-->
    <!--android:gravity="center"-->
    <!--android:layout_width="match_parent"-->
    <!--android:layout_height="match_parent" />-->
    <!--</FrameLayout>-->

    <android.support.design.widget.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:fitsSystemWindows="true"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer"/>

</android.support.v4.widget.DrawerLayout>

</LinearLayout>
```

完成修改后，MainActivity需要轻微修订一下 kotlinx 导入的资源，避免 `fab` 的import产生冲突：

```kotlin
//import kotlinx.android.synthetic.main.app_bar_main.*
```

现在，侧滑抽屉滑出时将不再会遮蔽工具栏了，同时当其滑出时，工具栏上的汉堡图标也会同时做旋转动画。

### 参考

- [DrawerLayout和NavigationView使用详解](https://www.jianshu.com/p/d2b1689a23bf)
- [打造自己的 APP「冰与火百科」（三）：Material Design 控件](https://juejin.im/post/59e17cbcf265da431522ef48)












































