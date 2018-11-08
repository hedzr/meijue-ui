package com.obsez.mobile.meijue.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.MenuItem
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.obsez.mobile.meijue.ui.base.fr.Entering
import com.obsez.mobile.meijue.ui.base.fr.FrameElements
import com.obsez.mobile.meijue.ui.ext.setHomeAsUpIndicatorWithBack
import com.obsez.mobile.meijue.ui.ext.setHomeAsUpIndicatorWithMenu
import timber.log.Timber


@Suppress("MemberVisibilityCanBePrivate", "unused")
open class BaseFragment : Fragment(), Entering {
    
    /**
     * 指示 Owner Activity 应该将 Home 按钮显示为 后退还是汉堡包
     */
    override val navigateBack: Boolean
        get() = false
    
    @Suppress("MemberVisibilityCanBePrivate")
    internal var defaultFrameElementsAction = true
    
    override fun onResume() {
        super.onResume()
        
        Timber.d("onResume: $this, to: $activity")
        
        if (activity != null) {
            if (navigateBack)
                (activity as AppCompatActivity).setHomeAsUpIndicatorWithBack()
            else
                (activity as AppCompatActivity).setHomeAsUpIndicatorWithMenu()
            
            if (activity is FrameElements)
                (activity as FrameElements).let {
                    doSetFrameElements(it)
                    onConnectToTabs(it)
                }
        }
    }
    
    override fun onPause() {
        super.onPause()
        
        Timber.d("onPause: $this, from: $activity")
        
        if (activity is FrameElements)
            (activity as FrameElements).let {
                onDisconnectFromTabs(it)
            }
    }
    
    //override fun onPrepareOptionsMenu(menu: Menu?) {
    //    super.onPrepareOptionsMenu(menu)
    //}
    
    override fun onConnectToTabs(frameElements: FrameElements) {
    
    }
    
    override fun onDisconnectFromTabs(frameElements: FrameElements) {
    
    }
    
    override fun onAttachToActivity(frameElements: FrameElements) {
        Timber.d("  -> onAttachToActivity: $this, into: $frameElements")
    }
    
    override fun doSetFrameElements(frameElements: FrameElements) {
        // do nothing by default
        if (defaultFrameElementsAction) {
            frameElements.apply {
                lockDrawer(false)
                setActionBarDrawerToggle()
                isCollapsingToolbarInExpandedMode = true
                motionFab(true)
                motionBnv(true)
            }
        }
    }
    
    
    // ----------
    
    
    fun FrameElements.setActionBarDrawerToggle() {
        actionBarDrawerToggleUi?.syncState()
        drawerLayoutUi?.closeDrawer(GravityCompat.START)
    }
    
    fun FrameElements.lockDrawer(lock: Boolean) {
        val lockMode = if (lock) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED
        drawerLayoutUi?.setDrawerLockMode(lockMode)
        
        actionBarDrawerToggleUi?.isDrawerIndicatorEnabled = lock
    }
    
    var FrameElements.isCollapsingToolbarInExpandedMode: Boolean
        get() = collapsingToolbarLayoutUi?.parent?.let {
            if (it is AppBarLayout) it.height == it.bottom else false
        } ?: false
        set(value) {
            collapsingToolbarLayoutUi?.parent?.let {
                if (it is AppBarLayout)
                    it.setExpanded(value, true)
            }
        }
    
    fun FrameElements.motionFab(appearance: Boolean) {
        if (appearance) {
            fabUi?.let {
                it.apply {
                    clearAnimation()
                }.animate()
                    .translationY(0f)
                    .scaleX(1f).scaleY(1f)
                    .alpha(1f)
                    .setInterpolator(OvershootInterpolator(1f))
                    .setStartDelay(300)
                    .setDuration(300L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            //
                        }
                    })
                    .start()
            }
        } else {
            fabUi?.let {
                it.apply {
                    clearAnimation()
                }.animate()
                    .scaleX(0f).scaleY(0f)
                    .alpha(0f)
                    .setDuration(100)
                    .setInterpolator(OvershootInterpolator(1f))
                    .setStartDelay(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            //
                        }
                    })
                    .start()
            }
        }
    }
    
    fun FrameElements.motionBnv(appearance: Boolean) {
        if (appearance) {
            bottomNavigationViewUi?.let {
                it.apply {
                    clearAnimation()
                }.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(OvershootInterpolator(1f))
                    .setStartDelay(200)
                    .setDuration(300L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            //
                        }
                    })
                    .start()
            }
        } else {
            bottomNavigationViewUi?.let {
                it.apply {
                    clearAnimation()
                }.animate()
                    .translationY(it.measuredHeight.toFloat())
                    .alpha(0f)
                    .setInterpolator(OvershootInterpolator(1f))
                    .setStartDelay(200)
                    .setDuration(300L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            //it.alpha = 0f
                        }
                    })
                    .start()
            }
        }
    }
    
    
    // ----------
    
    
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                activity?.also {
                    it.onBackPressed()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

