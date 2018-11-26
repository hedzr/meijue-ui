package com.obsez.mobile.meijue.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.obsez.mobile.meijue.ui.base.fr.Entering
import com.obsez.mobile.meijue.ui.base.fr.FrameElements
import com.obsez.mobile.meijue.ui.ext.*
import timber.log.Timber


@Suppress("MemberVisibilityCanBePrivate", "unused")
open class BaseFragment : Fragment(), Entering {
    
    /**
     * 指示 Owner Activity 应该将 Home 按钮显示为 后退还是汉堡包
     */
    override val navigateBack: Boolean
        get() = false
    
    /**
     * 指示 Owner Activity 应该将 Home 按钮显示为 白色还是黑色
     */
    override val navigateIconWhite: Boolean
        get() = false
    
    /**
     * 使用 Owner Activity 的 Tabs 还是Fragment自己定义的tabs;
     * 是否使用 Owner Activity 的 Tabs
     */
    override val useParentTabs: Boolean
        get() = false
    
    @Suppress("MemberVisibilityCanBePrivate")
    internal var defaultFrameElementsAction = true
    
    override fun onResume() {
        super.onResume()
        
        Timber.d("onResume: $this, to: $activity")
        
        if (activity != null) {
            (activity as AppCompatActivity).let { a ->
                if (navigateBack)
                    if (navigateIconWhite) a.setHomeAsUpIndicatorWithBackWhite() else a.setHomeAsUpIndicatorWithBack()
                else
                    if (navigateIconWhite) a.setHomeAsUpIndicatorWithMenuWhite() else a.setHomeAsUpIndicatorWithMenu()
            }
            
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
    
    // var View.layoutCollapseMode: Int
    //     get() {
    //         val params = layoutParams
    //         val newParams: CollapsingToolbarLayout.LayoutParams
    //         newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
    //             params
    //         } else {
    //             CollapsingToolbarLayout.LayoutParams(params)
    //         }
    //         return newParams.collapseMode
    //     }
    //     set(value) {
    //         val params = layoutParams
    //         val newParams: CollapsingToolbarLayout.LayoutParams
    //         newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
    //             params
    //         } else {
    //             CollapsingToolbarLayout.LayoutParams(params)
    //         }
    //         if (newParams.collapseMode != value) {
    //             newParams.collapseMode = value
    //             this.layoutParams = newParams
    //             this.requestLayout()
    //         }
    //     }
    
    override fun onConnectToTabs(frameElements: FrameElements) {
        Timber.v("onConnectToTabs()")
        frameElements.tabLayoutUi?.let { tabs ->
            if (useParentTabs) {
                //tabs.clearOnTabSelectedListeners()
                //tabs.removeAllTabs()
                
                tabs.layoutCollapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                tabs.visibility = View.VISIBLE
                
                frameElements.viewPagerUi?.let { vp ->
                    tabs.setupWithViewPager(vp)
                    //vp.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
                    //tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp))
                }
            } else {
                tabs.visibility = View.GONE
            }
            
        }
        frameElements.toolbarUi?.layoutCollapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
    }
    
    override fun onDisconnectFromTabs(frameElements: FrameElements) {
        Timber.v("onDisconnectFromTabs()")
        frameElements.tabLayoutUi?.let { tabs ->
            // tabs.visibility = View.INVISIBLE
            //tabs.removeAllTabs()
            //tabs.clearOnTabSelectedListeners()
            //view_pager.clearOnPageChangeListeners()
            if (useParentTabs) {
                tabs.setupWithViewPager(null)
            }
        }
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
                //tabLayoutUi?.apply { visibility = if (useParentTabs) View.VISIBLE else View.GONE }
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
        
        actionBarDrawerToggleUi?.isDrawerIndicatorEnabled = !lock
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

