package com.obsez.mobile.meijue.ui.ext

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageButton
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

//import com.obsez.mobile.meijue.ui.R
//
//fun test(context: Context) {
//    val btn = AppCompatImageButton(context)
//    btn.animateBounce(context.resources.getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha)) {
//        //
//    }
//    btn.animateBounce(
//        context.resources.getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha)
//    )
//}

/**
 * not a satisfied name.
 *
 * enlarge and restore an icon
 */
fun AppCompatImageButton.animateBounce(newImage: Drawable? = null, duration: Long = -1L, runnable: (() -> Unit)? = null) {
    this.animate().scaleX(1.4f).scaleY(1.4f)
        .setInterpolator(LinearOutSlowInInterpolator())
        .withEndAction {
            if (newImage != null)
                this.setImageDrawable(newImage)
            
            val ani = this.animate().scaleX(1f).scaleY(1f)
            if (duration > 0)
                ani.duration = duration
            if (runnable != null)
                ani.withEndAction(runnable)
            ani.start()
        }
        .start()
}

fun AppCompatImageButton.animateRotation(duration: Long = -1L, runnable: (() -> Unit)? = null) {
    val a = this.animate().rotationBy(360f)
        .scaleX(1.4f).scaleY(1.4f)
    if (duration > 0)
        a.duration = duration
    a.setInterpolator(LinearOutSlowInInterpolator())
        .withEndAction {
            val ani = this.animate()
                //.rotation(0f)
                .scaleX(1f).scaleY(1f)
            if (runnable != null)
            //.withEndAction { btn.animate().rotation(0f).setDuration(10).start() }
                ani.withEndAction(runnable)
            ani.start()
        }
        .start()
}

/**
 * not a satisfied name
 *
 * rotate an icon with enlarge it and restore it
 */
fun AppCompatImageButton.animateRotation2(duration: Long = -1L, runnable: (() -> Unit)? = null) {
    val a = this.animate().rotationBy(180f)
        .scaleX(1.4f).scaleY(1.4f)
    if (duration > 0)
        a.duration = duration
    a.setInterpolator(LinearOutSlowInInterpolator())
        .withEndAction {
            val ani = this.animate()
                .rotationBy(180f)
                .scaleX(1f).scaleY(1f)
            if (runnable != null)
            //.withEndAction { btn.animate().rotation(0f).setDuration(10).start() }
                ani.withEndAction(runnable)
            ani.start()
        }
        .start()
}


