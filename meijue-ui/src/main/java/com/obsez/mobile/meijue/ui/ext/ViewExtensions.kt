@file:Suppress("NOTHING_TO_INLINE")

package com.obsez.mobile.meijue.ui.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.obsez.mobile.meijue.ui.util.Utils
import org.jetbrains.annotations.NotNull


fun View.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, context.getString(id, args), duration).show()
fun View.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, msg, duration).show()
fun View.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(this, context.getString(id, args), duration); func?.let { s.it() }; s.show()
}

fun View.snackBar(@NotNull msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(this, msg, duration); func?.let { s.it() }; s.show()
}

fun View.setVisible(bool: Boolean?, nonVisibleState: Int = View.GONE) {
    visibility = if (bool == true) View.VISIBLE else nonVisibleState
}

var View.isVisible
    inline get() = visibility == View.VISIBLE
    inline set(value) = setVisible(value)


fun View.getPxSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun View.getColorById(@ColorRes id: Int) = ContextCompat.getColor(context, id)

fun View.getString(@StringRes id: Int): String = context.getString(id)

fun View.getString(@StringRes resId: Int, vararg formatArgs: Any): String {
    return context.getString(resId, *formatArgs)
}

fun View.addPadding(left: Int = 0, top: Int = 0, right: Int, bottom: Int = 0) {
    setPadding(left, top, right, bottom)
}

fun View.getDrawableById(@DrawableRes id: Int) = AppCompatResources.getDrawable(context, id)


val View.ctx: Context
    get() = context

var TextView.textColor: Int
    get() = currentTextColor
    set(v) = setTextColor(v)


///////////////////////////////////////////////////////////


inline var View.layoutScrollFlags: Int
    get() {
        val params = layoutParams
        val newParams: AppBarLayout.LayoutParams
        newParams = if (params is AppBarLayout.LayoutParams) {
            params
        } else {
            AppBarLayout.LayoutParams(params)
        }
        return newParams.scrollFlags
    }
    set(value) {
        val params = layoutParams
        val newParams: AppBarLayout.LayoutParams
        newParams = if (params is AppBarLayout.LayoutParams) {
            params
        } else {
            AppBarLayout.LayoutParams(params)
        }
        if (newParams.scrollFlags != value) {
            newParams.scrollFlags = value
            this.layoutParams = newParams
            //this.requestLayout()
        }
    }


inline var View.layoutCollapseMode: Int
    get() {
        val params = layoutParams
        val newParams: CollapsingToolbarLayout.LayoutParams
        newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
            params
        } else {
            CollapsingToolbarLayout.LayoutParams(params)
        }
        return newParams.collapseMode
    }
    set(value) {
        val params = layoutParams
        val newParams: CollapsingToolbarLayout.LayoutParams
        newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
            params
        } else {
            CollapsingToolbarLayout.LayoutParams(params)
        }
        if (newParams.collapseMode != value) {
            newParams.collapseMode = value
            this.layoutParams = newParams
            //this.requestLayout()
        }
    }

inline var View.layoutParallaxMultiplier: Float
    get() {
        val params = layoutParams
        val newParams: CollapsingToolbarLayout.LayoutParams
        newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
            params
        } else {
            CollapsingToolbarLayout.LayoutParams(params)
        }
        return newParams.parallaxMultiplier
    }
    set(value) {
        val params = layoutParams
        val newParams: CollapsingToolbarLayout.LayoutParams
        newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
            params
        } else {
            CollapsingToolbarLayout.LayoutParams(params)
        }
        if (newParams.parallaxMultiplier != value) {
            newParams.parallaxMultiplier = value
            this.layoutParams = newParams
            //this.requestLayout()
        }
    }


inline var View.behavior: CoordinatorLayout.Behavior<*>?
    get() {
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            return (layoutParams as CoordinatorLayout.LayoutParams).behavior
        }
        return null
    }
    set(value) {
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            (layoutParams as CoordinatorLayout.LayoutParams).behavior = value
        }
    }


/**
 * setMargins in dp
 */
fun View.setMargins(l: Int = -1, t: Int = -1, r: Int = -1, b: Int = -1) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(
            if (l != -1) l else p.leftMargin,
            if (t != -1) t else p.topMargin,
            if (r != -1) r else p.rightMargin,
            if (b != -1) b else p.bottomMargin
        )
        requestLayout()
    }
}

/**
 * setMargins in dp
 */
fun View.setMarginsInDp(l: Int = -1, t: Int = -1, r: Int = -1, b: Int = -1) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(
            if (l != -1) Utils.dp2px(l) else p.leftMargin,
            if (t != -1) Utils.dp2px(t) else p.topMargin,
            if (r != -1) Utils.dp2px(r) else p.rightMargin,
            if (b != -1) Utils.dp2px(b) else p.bottomMargin
        )
        requestLayout()
    }
}


/**
 * 获得绝对坐标
 */
val View.locationOnScreen: IntArray
    inline get() {
        val loc = IntArray(2)
        this.getLocationOnScreen(loc)
        return loc
    }

/**
 * @deprecated
 */
val View.yAbs: Int
    get() {
        // 会无限循环，尚未进一步解决
        var y = top
        var p: View? = this.parent as View
        while (p != null) {
            y += p.top
            if (p.parent is View) {
                p = p.parent as View
            } else {
                break
            }
        }
        return y
    }

/**
 * @deprecated
 */
val View.xAbs: Int
    get() {
        // 会无限循环，尚未进一步解决
        var x = left
        var p: View? = this.parent as View
        while (p != null) {
            x += p.left
            if (p.parent is View) {
                p = p.parent as View
            } else {
                break
            }
        }
        return x
    }


////////////////////////////////////////////////////////////


