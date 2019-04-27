package com.obsez.mobile.meijue.ui.ext

import android.graphics.Color
import androidx.annotation.ColorInt
import java.text.DateFormat
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KType

const val NETWORK_TIMEOUT_SECONDS = 30L
const val DB_TIMEOUT_SECONDS = 2L

const val USERNAME_PATTERN = "(?<![0-9a-zA-Z])(@\\w+)"


fun KType.isClass(cls: KClass<*>): Boolean {
    return this.classifier == cls
}

val KType.isTypeString: Boolean get() = this.isClass(String::class)
val KType.isTypeInt: Boolean get() = this.isClass(Int::class) || this.isClass(java.lang.Integer::class)
val KType.isTypeLong: Boolean get() = this.isClass(Long::class) || this.isClass(java.lang.Long::class)
val KType.isTypeByte: Boolean get() = this.isClass(Byte::class) || this.isClass(java.lang.Byte::class)
val KType.isTypeShort: Boolean get() = this.isClass(Short::class) || this.isClass(java.lang.Short::class)
val KType.isTypeChar: Boolean get() = this.isClass(Char::class) || this.isClass(java.lang.Character::class)
val KType.isTypeBoolean: Boolean get() = this.isClass(Boolean::class) || this.isClass(java.lang.Boolean::class)
val KType.isTypeFloat: Boolean get() = this.isClass(Float::class) || this.isClass(java.lang.Float::class)
val KType.isTypeDouble: Boolean get() = this.isClass(Double::class) || this.isClass(java.lang.Double::class)
val KType.isTypeByteArray: Boolean get() = this.isClass(ByteArray::class)


// //////////////////////////////////////////


object Objects {
    fun equals(o1: Any?, o2: Any?): Boolean {
        if (o1 == null) {
            return o2 == null
        }
        return if (o2 == null) {
            false
        } else o1 == o2
    }
}


// //////////////////////////////////////////


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
class AutoClearedValue<T>(fragment: androidx.fragment.app.Fragment, private var value: T?) {
    
    init {
        val fragmentManager = fragment.fragmentManager!!
        fragmentManager.registerFragmentLifecycleCallbacks(
            object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment) {
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
