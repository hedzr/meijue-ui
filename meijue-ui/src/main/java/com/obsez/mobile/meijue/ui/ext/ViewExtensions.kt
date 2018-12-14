@file:Suppress("NOTHING_TO_INLINE")

package com.obsez.mobile.meijue.ui.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.obsez.mobile.meijue.ui.R
import com.obsez.mobile.meijue.ui.util.Utils
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import org.jetbrains.annotations.NotNull


fun View.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, context.getString(id, args), duration).show()
fun View.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, msg, duration).show()
fun View.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(this, context.getString(id, args), duration); func?.let { s.it() }; s.show()
}

fun View.snackBar(@NotNull msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG, func: (Snackbar.() -> Unit)? = null) {
    val s = Snackbar.make(this, msg, duration); func?.let { s.it() }; s.show()
}

fun View.isVisible(bool: Boolean?, nonVisibleState: Int = View.GONE) {
    visibility = if (bool == true) View.VISIBLE else nonVisibleState
}

val View.isVisible
    inline get() = visibility == View.VISIBLE


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

fun View.slideExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}

fun View.fadeOut(duration: Long = 500): ViewPropertyAnimator {
    return animate()
        .alpha(0.0f)
        .setDuration(duration)
}

fun View.fadeIn(duration: Long = 500): ViewPropertyAnimator {
    return animate()
        .alpha(1.0f)
        .setDuration(duration)
}


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


fun ViewGroup.asSequence(): Sequence<View> = object : Sequence<View> {
    
    override fun iterator(): Iterator<View> = object : Iterator<View> {
        private var nextValue: View? = null
        private var done = false
        private var position: Int = 0
        
        override public fun hasNext(): Boolean {
            if (nextValue == null && !done) {
                nextValue = getChildAt(position)
                position++
                if (nextValue == null) done = true
            }
            return nextValue != null
        }
        
        override fun next(): View {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val answer = nextValue
            nextValue = null
            return answer!!
        }
    }
}

/**
 * ref. https://www.youtube.com/watch?v=fPzxfeDJDzY
 */

inline fun ViewGroup.inflate(@LayoutRes l: Int): View =
    LayoutInflater.from(context).inflate(l, this, false)

inline operator fun ViewGroup.get(i: Int): View? = getChildAt(i)

/**
 * -=
 */
inline operator fun ViewGroup.minusAssign(@NonNull child: View) = removeView(child)

/**
 * +=
 */
inline operator fun ViewGroup.plusAssign(@NonNull child: View) = addView(child)

/**
 * if (viwe in views)
 */
inline fun ViewGroup.contains(@NonNull child: View) = indexOfChild(child) != -1

inline fun ViewGroup.first(): View? = this[0]

inline fun ViewGroup.forEach(@NonNull action: (View) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachIndexed(@NonNull action: (Int, View) -> Unit) {
    for (i in 0 until childCount) {
        action(i, getChildAt(i))
    }
}

/**
 * for (view in views.children())
 */
inline fun ViewGroup.children() = object : Iterable<View> {
    override fun iterator() = object : Iterator<View> {
        var index = 0
        override fun hasNext() = index < childCount
        override fun next() = getChildAt(index++)
    }
}


fun ImageView.loadUrlViaPicasso(url: String, func: (RequestCreator.() -> RequestCreator)? = null) {
    val rc = Picasso.with(context).load(url)
    if (func != null) {
        rc.func()
    }
    rc.into(this)
}

/**
 * loadUrlViaGlide("http://...") {
 *     diskCacheStrategy(DiskCacheStrategy.ALL) // 改善磁盘缓存策略，避免多次下载同一图片
 *     override(300, 200) // resize the image
 *     centerCrop()
 *     transform(CircleTransform(context))
 *     placeholder(R.drawable.placeholder)
 *     error(R.drawable.image_not_found)
 * }
 *
 */
fun ImageView.loadUrlViaGlide(url: String, func: (RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>)? = null) {
    val rb = Glide.with(context).load(url)
    func?.let { rb.it() }
    rb.into(this)
}

/**
 * 使用 Fresco 需要事先学习相关概念。
 *
 * https://www.jianshu.com/p/bb32bca8796b
 * https://blog.csdn.net/tiankongcheng6/article/details/53884611
 * https://fucknmb.com/2017/07/27/%E4%B8%80%E7%A7%8D%E4%BD%BF%E7%94%A8Fresco%E9%9D%9E%E4%BE%B5%E5%85%A5%E5%BC%8F%E5%8A%A0%E8%BD%BD%E5%9B%BE%E7%89%87%E7%9A%84%E6%96%B9%E5%BC%8F/
 *
 * TODO 完整地支持 Fresco 需要以后安排时间
 *
 */
fun ImageView.loadUrlViaFresco(url: String, func: (RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>)? = null) {
    Fresco.initialize(this.context)
    val rb = Glide.with(context).load(url)
    if (func != null) {
        rb.func()
    }
    rb.into(this)
}


////////////////////////////////////////////////////////////


/**
 * Helper class that iterates through Toolbar views, and sets dynamically icons and texts color
 * Created by chomi3 on 2015-01-19.
 */
object ToolbarColorizeHelper {
    
    /**
     * Use this method to colorize toolbar icons to the desired target color
     * @param toolbarView toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     * @param activity reference to activity needed to register observers
     */
    fun colorizeToolbar(toolbarView: Toolbar, toolbarIconsColor: Int, activity: Activity) {
        val colorFilter = PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY)
        
        for (i in 0 until toolbarView.childCount) {
            val v = toolbarView.getChildAt(i)
            
            //Step 1 : Changing the color of back button (or open drawer button).
            if (v is ImageButton) {
                //Action Bar back button
                v.drawable.colorFilter = colorFilter
            }
            
            
            if (v is ActionMenuView) {
                for (j in 0 until v.childCount) {
                    
                    //Step 2: Changing the color of any ActionMenuViews - icons that are not back button, nor text, nor overflow menu icon.
                    //Colorize the ActionViews -> all icons that are NOT: back button | overflow menu
                    val innerView = v.getChildAt(j)
                    if (innerView is ActionMenuItemView) {
                        for (k in 0 until innerView.compoundDrawables.size) {
                            if (innerView.compoundDrawables[k] != null) {
                                
                                //Important to set the color filter in seperate thread, by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post { innerView.compoundDrawables[k].colorFilter = colorFilter }
                            }
                        }
                    }
                }
            }
            
            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor)
            toolbarView.setSubtitleTextColor(toolbarIconsColor)
            
            //Step 4: Changing the color of the Overflow Menu icon.
            setOverflowButtonColor(activity, colorFilter)
        }
    }
    
    /**
     * It's important to set overflowDescription atribute in styles, so we can grab the reference
     * to the overflow icon. Check: res/values/styles.xml
     * @param activity
     * @param colorFilter
     */
    private fun setOverflowButtonColor(activity: Activity, colorFilter: PorterDuffColorFilter) {
        val overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description)
        val decorView = activity.window.decorView as ViewGroup
        val viewTreeObserver = decorView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val outViews = ArrayList<View>()
                decorView.findViewsWithText(outViews, overflowDescription,
                    View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
                if (outViews.isEmpty()) {
                    return
                }
                val overflowP: ActionMenuView = outViews[0].parent as ActionMenuView
                overflowP.overflowIcon?.colorFilter = colorFilter // PorterDuff.Mode.SRC_ATOP
                removeOnGlobalLayoutListener(decorView, this)
            }
        })
    }
    
    @Suppress("DEPRECATION")
    @SuppressLint("ObsoleteSdkInt")
    private fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        } else {
            v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}


/**
 * https://snow.dog/blog/how-to-dynamicaly-change-android-toolbar-icons-color/
 */
fun Toolbar.colorize(toolbarIconsColor: Int) {
    ToolbarColorizeHelper.colorizeToolbar(this, toolbarIconsColor, this.context as Activity)
}


