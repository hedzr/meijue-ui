package com.obsez.mobile.meijue.ui.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.Transformation


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

fun View.fadeInOfficial(duration: Long = 300, animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.visibility = View.GONE
        v.alpha = 0.0f
        // Prepare the View for the animation
        v.animate()
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.VISIBLE
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1.0f)
            .start()
    }
}

fun View.fadeOutOfficial(duration: Long = 500, animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.alpha = 1.0f
        // Prepare the View for the animation
        v.animate()
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animListener?.invoke(v)
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(0.0f)
            .start()
    }
}

fun View.fadeOutIn() {
    this.let { v ->
        v.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorAlpha = ObjectAnimator.ofFloat(v, "alpha", 0f, 0.5f, 1f)
        ObjectAnimator.ofFloat(v, "alpha", 0f).start()
        animatorAlpha.duration = 500
        animatorSet.play(animatorAlpha)
        animatorSet.start()
    }
}


fun View.animScaleIn(animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.animate()
            .scaleY(1f)
            .scaleX(1f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .start()
    }
}

fun View.animScaleOut(animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.animate()
            .scaleY(0f)
            .scaleX(0f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .start()
    }
}


fun View.animShowIn(animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1f)
            .start()
    }
}

fun View.initShowOut() {
    this.let { v ->
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }
}

fun View.animShowOut(animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
            .setDuration(200)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
    }
}

fun View.animRotateFab(rotate: Boolean, animListener: ((View) -> Unit)? = null): Boolean {
    this.let { v ->
        v.animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .rotation(if (rotate) 135f else 0f)
    }
    return rotate
}


fun View.animExpand(animListener: ((View) -> Unit)? = null) {
    val a = expandAction(this)
    if (animListener != null)
        a.setAnimationListener(object : Animation.AnimationListener {
            /**
             *
             * Notifies the repetition of the animation.
             *
             * @param animation The animation which was repeated.
             */
            override fun onAnimationRepeat(animation: Animation?) {
            }
            
            /**
             *
             * Notifies the end of the animation. This callback is not invoked
             * for animations with repeat count set to INFINITE.
             *
             * @param animation The animation which reached its end.
             */
            override fun onAnimationEnd(animation: Animation?) {
                animListener(this@animExpand)
            }
            
            /**
             *
             * Notifies the start of the animation.
             *
             * @param animation The started animation.
             */
            override fun onAnimationStart(animation: Animation?) {
            }
        })
    this.startAnimation(a)
}

fun expandAction(v: View): Animation {
    v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val targetHeight = v.measuredHeight
    
    v.layoutParams.height = 0
    v.visibility = View.VISIBLE
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            v.layoutParams.height = if (interpolatedTime == 1f)
                ViewGroup.LayoutParams.WRAP_CONTENT
            else
                (targetHeight * interpolatedTime).toInt()
            v.requestLayout()
        }
        
        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    
    a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
    //v.startAnimation(a)
    return a
}

fun View.animCollapse(animListener: ((View) -> Unit)? = null) {
    val initialHeight = this.measuredHeight
    
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                this@animCollapse.visibility = View.GONE
            } else {
                this@animCollapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                this@animCollapse.requestLayout()
            }
        }
        
        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    
    if (animListener != null)
        a.setAnimationListener(object : Animation.AnimationListener {
            /**
             *
             * Notifies the repetition of the animation.
             *
             * @param animation The animation which was repeated.
             */
            override fun onAnimationRepeat(animation: Animation?) {
            }
            
            /**
             *
             * Notifies the end of the animation. This callback is not invoked
             * for animations with repeat count set to INFINITE.
             *
             * @param animation The animation which reached its end.
             */
            override fun onAnimationEnd(animation: Animation?) {
                animListener(this@animCollapse)
            }
            
            /**
             *
             * Notifies the start of the animation.
             *
             * @param animation The started animation.
             */
            override fun onAnimationStart(animation: Animation?) {
            }
        })
    
    a.duration = (initialHeight / context.resources.displayMetrics.density).toInt().toLong()
    this.startAnimation(a)
}

fun View.flyInDown(duration: Long = 200L, animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.visibility = View.VISIBLE
        v.alpha = 0.0f
        v.translationY = 0f
        v.translationY = (-v.height).toFloat()
        // Prepare the View for the animation
        v.animate()
            .setDuration(duration)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1.0f)
            .start()
    }
}

fun View.flyOutDown(duration: Long = 200L, animListener: ((View) -> Unit)? = null) {
    this.let { v ->
        v.visibility = View.VISIBLE
        v.alpha = 1.0f
        v.translationY = 0f
        // Prepare the View for the animation
        v.animate()
            .setDuration(duration)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animListener != null) animListener(v)
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(0.0f)
            .start()
    }
}


fun View.toggleArrow(): Boolean {
    return if (this.rotation == 0f) {
        this.animate().setDuration(200).rotation(180f)
        true
    } else {
        this.animate().setDuration(200).rotation(0f)
        false
    }
}

fun View.toggleArrow(show: Boolean, delay: Boolean = true): Boolean {
    return if (show) {
        this.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(180f)
        true
    } else {
        this.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(0f)
        false
    }
}


