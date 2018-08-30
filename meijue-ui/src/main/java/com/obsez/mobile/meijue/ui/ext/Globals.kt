package com.obsez.mobile.meijue.ui.ext

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import java.text.DateFormat
import java.util.*

const val NETWORK_TIMEOUT_SECONDS = 30L
const val DB_TIMEOUT_SECONDS = 2L

const val USERNAME_PATTERN = "(?<![0-9a-zA-Z])(@\\w+)"


object ColorExt {
    fun isLightColor(@ColorInt color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.5
    }
}


fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}


fun now() = Date()

fun nowMillis() = System.currentTimeMillis()

fun Int.toBoolean() = this > 0

fun Boolean.toInt() = if (this) 1 else 0

/**
 * Assume that this Int is a color and apply [opacity] to it.
 */
@ColorInt
fun Int.withOpacity(/*@IntRange(from = 0, to = 100)*/ opacity: Int): Int {
    return Color.argb((opacity / 100f * 255).toInt(), Color.red(this), Color.green(this), Color.blue(this))
}


/**
 * A value holder that automatically clears the reference if the Fragment's view is destroyed.
 */
class AutoClearedValue<T>(fragment: Fragment, private var value: T?) {
    
    init {
        val fragmentManager = fragment.fragmentManager!!
        fragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        if (f === fragment) {
                            this@AutoClearedValue.value = null
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                        }
                    }
                }, false)
    }
    
    fun get(): T? {
        return value
    }
}
