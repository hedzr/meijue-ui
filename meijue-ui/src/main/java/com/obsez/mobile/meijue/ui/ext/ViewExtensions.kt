@file:Suppress("NOTHING_TO_INLINE")

package com.obsez.mobile.meijue.ui.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.facebook.drawee.backends.pipeline.Fresco
import com.obsez.mobile.meijue.ui.util.Utils
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import org.jetbrains.annotations.NotNull


fun View.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, context.getString(id, args), duration).show()
fun View.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, msg, duration).show()
fun View.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = Snackbar.LENGTH_LONG, func: Snackbar.() -> Unit) {
    val s = Snackbar.make(this, context.getString(id, args), duration); s.func(); s.show()
}

fun View.snackBar(@NotNull msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG, func: Snackbar.() -> Unit) {
    val s = Snackbar.make(this, msg, duration); s.func(); s.show()
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
inline operator fun ViewGroup.minusAssign(child: View) = removeView(child)

/**
 * +=
 */
inline operator fun ViewGroup.plusAssign(child: View) = addView(child)

/**
 * if (viwe in views)
 */
inline fun ViewGroup.contains(child: View) = indexOfChild(child) != -1

inline fun ViewGroup.first(): View? = this[0]

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachIndexed(action: (Int, View) -> Unit) {
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


fun ImageView.loadUrlViaPicasso(url: String, func: RequestCreator.() -> RequestCreator) {
    Picasso.with(context).load(url).func().into(this)
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
fun ImageView.loadUrlViaGlide(url: String, func: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>) {
    Glide.with(context).load(url).func().into(this)
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
fun ImageView.loadUrlViaFresco(url: String, func: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>) {
    Fresco.initialize(this.context)
    Glide.with(context).load(url).func().into(this)
}




