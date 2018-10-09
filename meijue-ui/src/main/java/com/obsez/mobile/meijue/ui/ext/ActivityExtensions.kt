package com.obsez.mobile.meijue.ui.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.annotations.NotNull


inline fun <reified T : Activity> Activity.navigate(id: String) {
    val intent = Intent(this, T::class.java)
    intent.putExtra("id", id)
    startActivity(intent)
}

fun Activity.hideKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS)
    }
    return false
}


/**
 * Usage:
 *     toast(xxx)
 *     toast(xxx, Toast.LENGTH_LONG)
 */
fun Activity.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, getString(id, args), duration).show()

fun Activity.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, duration).show()
/**
 * Usage:
 *     snackBar("I'm here")
 *     snackBar("I'm here") {
 *         action("Retry?"){
 *             toast("retrying...")
 *         }
 *     }
 */
fun Activity.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(rootContentView, getString(id, args), duration); func?.let { s.it() }; s.show()
}

fun Activity.snackBar(@NotNull msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(rootContentView, msg, duration); func?.let { s.it() }; s.show()
}


@SuppressLint("ObsoleteSdkInt")
fun Activity.makeAppFullscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.TRANSPARENT
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

/**
 * Usage:
 *     startActivity<Main2Activity>()
 *     startActivity<Main2Activity>(intent)
 *     startActivity<MenuActivity> {
 *         putExtra("ss", 1)
 *     }
 *
 */
//inline fun <reified T> Activity.startActivity(func: Intent.() -> Intent) = startActivity(Intent(this, T::class.java).func())

// from: toshi
inline fun <reified T> Activity.startActivity() = startActivity(Intent(this, T::class.java))

/**
 * Usage (in activity):
 *
 * startActivitySE<MenuActivity>(
 *     Pair.create(mLogoView, MenuActivity.SHARED_REST_LOGO),
 *     Pair.create(mTextView, MenuActivity.SHARED_REST_TITLE)
 * ) {
 *     putExtra("ss", 1)
 *     putExtra(MenuActivity.EXTRA_REST_ID, 0)
 * }
 *
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
inline fun <reified T> AppCompatActivity.startActivitySE(vararg sharedElements: androidx.core.util.Pair<View, String>, noinline func: (Intent.() -> Intent)? = null) {
    val intent = Intent(this, T::class.java)
    if (func != null) intent.func()
    if (sharedElements.isEmpty()) {
        startActivity(intent)
        return
    }
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *sharedElements)
    startActivity(intent, options.toBundle())
}

/**
 * Usage (in activity):
 *
 * startActivity<MenuActivity>(mLogoView, mTextView) {
 *     putExtra("ss", 1)
 *     putExtra(MenuActivity.EXTRA_REST_ID, 0)
 * }
 *
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
inline fun <reified T> AppCompatActivity.startActivity(vararg sharedElements: View?, noinline func: (Intent.() -> Intent)? = null) {
    val intent = Intent(this, T::class.java)
    if (func != null) intent.func()
    if (sharedElements.isEmpty()) {
        startActivity(intent)
        return
    }

    //val elements = emptyArray<Pair<View, String>>()
    val elements = Array(sharedElements.size) { androidx.core.util.Pair(this.rootContentView, "") }
    for (i in 0 until sharedElements.size) {
        sharedElements[i]?.let { elements[i] = androidx.core.util.Pair(it, ViewCompat.getTransitionName(it)) }
    }
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *elements)
    startActivity(intent, options.toBundle())
}

inline fun <reified T> Activity.startActivityAndFinish(noinline func: (Intent.() -> Intent)? = null) {
    startActivity(func?.let { Intent(this, T::class.java).it() })
    finish()
}

inline fun <reified T> Activity.startActivityAndFinish() {
    startActivity(Intent(this, T::class.java))
    finish()
}

inline fun <reified T> Activity.startActivityForResult(requestCode: Int, noinline func: (Intent.() -> Intent)? = null) {
    startActivityForResult(func?.let { Intent(this, T::class.java).it() }, requestCode)
}

@Suppress("NOTHING_TO_INLINE")
inline fun Activity.setActivityResultAndFinish(resultCode: Int, noinline func: (Intent.() -> Intent)? = null) {
    setResult(resultCode, func?.let { intent.it() })
    finish()
}

fun Activity.setActivityResultAndFinish(resultCode: Int) {
    setResult(resultCode, intent)
    finish()
}

fun Activity.getColorById(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Activity.getDrawableById(@DrawableRes id: Int) = AppCompatResources.getDrawable(this, id)

fun Activity.hideStatusBar() = window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

fun Activity.getPxSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Activity.getMultiplePxSize(@DimenRes id1: Int, @DimenRes id2: Int): Int {
    return resources.getDimensionPixelSize(id1) + resources.getDimensionPixelSize(id2)
}

fun Activity.getMultiplePxSize(@DimenRes id1: Int, @DimenRes id2: Int, @DimenRes id3: Int): Int {
    return resources.getDimensionPixelSize(id1) + resources.getDimensionPixelSize(id2) + resources.getDimensionPixelSize(id3)
}

fun getAbsoluteY(view: View): Int {
    val coords = IntArray(2)
    view.getLocationInWindow(coords)
    return coords[1]
}

//fun AppCompatActivity.openWebView(address: String) {
//    startActivity<LollipopWebViewActivity> {
//        putExtra(LollipopWebViewActivity.EXTRA__ADDRESS, address)
//    }
//}
//
//inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(): T {
//    return ViewModelProviders.of(this).get(T::class.java)
//}
//
//inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(crossinline factory: () -> T): T {
//    val vmFactory = object : ViewModelProvider.Factory {
//        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
//    }
//    return ViewModelProviders.of(this, vmFactory)[T::class.java]
//}


fun Activity.setTranslucentMode(translucentMode: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (translucentMode) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}


/**
 * `setContentFragment` enable you load certain a Fragment right away after `setContentView` done.
 *
 */
inline fun androidx.fragment.app.FragmentActivity.setContentFragment(containerViewId: Int, f: () -> androidx.fragment.app.Fragment): androidx.fragment.app.Fragment? {
    val manager = supportFragmentManager
    val fragment = manager?.findFragmentById(containerViewId)
    fragment?.let { return it }
    return f().apply { manager?.beginTransaction()?.add(containerViewId, this)?.commit() }
}


/**
 * 获取的是 setContentView() 送入的布局资源的顶级视图。
 * 该视图为 android.support.v7.widget.ContentFrameLayout
 */
val Activity.rootContentView: View
    inline get() {
        return window.decorView.findViewById(android.R.id.content)
    }

val Activity.rootContentViewGroup: ViewGroup
    inline get() {
        val v = window.decorView.findViewById<ViewGroup>(android.R.id.content)
        return v.getChildAt(0) as ViewGroup
    }



