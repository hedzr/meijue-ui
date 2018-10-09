package com.obsez.mobile.meijue.ui.ext

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.annotations.NotNull

//inline fun <reified T> Fragment.startActivity(func: Intent.() -> Intent) = startActivity(Intent(activity, T::class.java).func())

inline fun <reified T> androidx.fragment.app.Fragment.startActivity() = startActivity(Intent(activity, T::class.java))

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
inline fun <reified T> androidx.fragment.app.Fragment.startActivitySE(vararg sharedElements: androidx.core.util.Pair<View, String>, noinline func: (Intent.() -> Intent)? = null) {
    val intent = Intent(activity, T::class.java)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, *sharedElements)
    startActivity(func?.let { intent.it() }, options.toBundle())
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
inline fun <reified T> androidx.fragment.app.Fragment.startActivity(vararg sharedElements: View?, noinline func: (Intent.() -> Intent)? = null) {
    val intent = Intent(activity, T::class.java)
    //val elements = emptyArray<Pair<View, String>>()
    val elements = Array(sharedElements.size) { Pair(this.view, "") }
    for (i in 0 until sharedElements.size) {
        sharedElements[i]?.let { elements[i] = androidx.core.util.Pair(it, ViewCompat.getTransitionName(it)) }
    }
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, *elements)
    startActivity(func?.let { intent.it() }, options.toBundle())
}


inline fun <reified T> androidx.fragment.app.Fragment.startActivityForResult(requestCode: Int, noinline func: (Intent.() -> Intent)? = null) {
    val intent = func?.let { Intent(activity, T::class.java).it() }
    startActivityForResult(intent, requestCode)
}

fun androidx.fragment.app.Fragment.startExternalActivity(func: (Intent.() -> Intent)? = null) = startActivity(func?.let { Intent().it() }, null)

inline fun <reified T> androidx.fragment.app.Fragment.startActivityAndFinish() {
    startActivity(Intent(activity, T::class.java))
    activity?.finish()
}


fun androidx.fragment.app.Fragment.getPxSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun androidx.fragment.app.Fragment.getColorById(@ColorRes id: Int) = context?.let { ContextCompat.getColor(it, id) }

fun androidx.fragment.app.Fragment.getColor(@ColorRes id: Int) = context?.let { ContextCompat.getColor(it, id) }


fun androidx.fragment.app.Fragment.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(activity, activity?.resources?.getString(id, args), duration).show()
fun androidx.fragment.app.Fragment.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(activity, msg, duration).show()
fun androidx.fragment.app.Fragment.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = com.google.android.material.snackbar.Snackbar.make(view!!, activity?.resources?.getString(id, args)!!, duration); func?.let { s.it() }; s.show()
}

fun androidx.fragment.app.Fragment.snackBar(@NotNull msg: CharSequence, duration: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = com.google.android.material.snackbar.Snackbar.make(view!!, msg, duration); func?.let { s.it() }; s.show()
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

inline val androidx.fragment.app.Fragment.rootContentView: View?
    get() {
        return activity?.window?.decorView?.findViewById(android.R.id.content)
    }

