package com.obsez.mobile.meijue.ui.ext

import android.content.Intent
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.Toast
import org.jetbrains.annotations.NotNull

//inline fun <reified T> Fragment.startActivity(func: Intent.() -> Intent) = startActivity(Intent(activity, T::class.java).func())

inline fun <reified T> Fragment.startActivity() = startActivity(Intent(activity, T::class.java))

/**
 * Usage (in activity):
 *
 * startActivitySE<MenuActivity>(Pair.create(mLogoView, MenuActivity.SHARED_REST_LOGO),
 *     Pair.create(mTextView, MenuActivity.SHARED_REST_TITLE)
 * ) {
 *     putExtra("ss", 1)
 *     putExtra(MenuActivity.EXTRA_REST_ID, 0)
 * }
 *
 */
inline fun <reified T> Fragment.startActivitySE(vararg sharedElements: android.support.v4.util.Pair<View, String>, func: Intent.() -> Intent) {
    val intent = Intent(activity, T::class.java)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, *sharedElements)
    startActivity(intent.func(), options.toBundle())
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
inline fun <reified T> Fragment.startActivity(vararg sharedElements: View?, func: Intent.() -> Intent) {
    val intent = Intent(activity, T::class.java)
    //val elements = emptyArray<Pair<View, String>>()
    val elements = Array(sharedElements.size) { Pair(this.view, "") }
    for (i in 0 until sharedElements.size) {
        sharedElements[i]?.let { elements[i] = android.support.v4.util.Pair(it, ViewCompat.getTransitionName(it)) }
    }
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, *elements)
    startActivity(intent.func(), options.toBundle())
}


inline fun <reified T> Fragment.startActivityForResult(requestCode: Int, func: Intent.() -> Intent) {
    val intent = Intent(activity, T::class.java).func()
    startActivityForResult(intent, requestCode)
}

fun Fragment.startExternalActivity(func: Intent.() -> Intent) = startActivity(Intent().func(), null)

inline fun <reified T> Fragment.startActivityAndFinish() {
    startActivity(Intent(activity, T::class.java))
    activity?.finish()
}


fun Fragment.getPxSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Fragment.getColorById(@ColorRes id: Int) = context?.let { ContextCompat.getColor(it, id) }

fun Fragment.getColor(@ColorRes id: Int) = context?.let { ContextCompat.getColor(it, id) }


fun Fragment.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(activity, activity?.resources?.getString(id, args), duration).show()
fun Fragment.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(activity, msg, duration).show()
fun Fragment.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(view!!, activity?.resources?.getString(id, args)!!, duration); func?.let { s.it() }; s.show()
}

fun Fragment.snackBar(@NotNull msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(view!!, msg, duration); func?.let { s.it() }; s.show()
}


//fun Fragment.openWebView(address: String) {
//    startActivity<LollipopWebViewActivity> {
//        putExtra(LollipopWebViewActivity.EXTRA__ADDRESS, address)
//    }
//}
//
//fun Fragment.openWebViewForResult(requestCode: Int, address: String) {
//    startActivityForResult<LollipopWebViewActivity>(requestCode) {
//        putExtra(LollipopWebViewActivity.EXTRA__ADDRESS, address)
//        putExtra(LollipopWebViewActivity.EXIT_ACTION, true)
//    }
//}

inline val Fragment.rootContentView: View?
    get() {
        return activity?.window?.decorView?.findViewById(android.R.id.content)
    }

