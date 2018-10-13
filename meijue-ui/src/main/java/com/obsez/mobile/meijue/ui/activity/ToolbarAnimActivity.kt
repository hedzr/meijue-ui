package com.obsez.mobile.meijue.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.obsez.mobile.meijue.ui.R
import com.obsez.mobile.meijue.ui.util.Utils

@Suppress("PrivatePropertyName", "RedundantVisibilityModifier")
public abstract class ToolbarAnimActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    @Suppress("MemberVisibilityCanBePrivate", "PropertyName")
    protected val ANIM_DURATION_TOOLBAR = 300L
    @Suppress("MemberVisibilityCanBePrivate", "PropertyName")
    protected val ANIM_DURATION_FAB = 400L
    
    @Suppress("MemberVisibilityCanBePrivate")
    protected var pendingIntroAnimation: Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            pendingIntroAnimation = true
        }
    }
    
    //    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    //        super.onCreate(savedInstanceState, persistentState)
    //    }
    
    override fun setContentView(layoutResID: Int) {
        superSetContentView(layoutResID)
        setupAppBar()
        setupToolbar()
        afterSetupToolbar()
        if (pendingIntroAnimation) setupFab()
        setupNavDrawer()
        setupTabs()
        setupViewPager()
        setupBottomSheetView()
        setupBottomNav()
        postSetContentView(layoutResID)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val ret = super.onCreateOptionsMenu(menu)
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false
            startIntroAnimation()
        }
        return ret
    }
    
    override fun onBackPressed() {
        if (drawerLayoutUi != null) {
            drawerLayoutUi?.let {
                if (it.isDrawerOpen(GravityCompat.START)) {
                    it.closeDrawer(GravityCompat.START)
                    return
                }
            }
        }
        super.onBackPressed()
    }
    
    fun superOnBackPressed() {
        super.onBackPressed()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                //mDrawerLayout.openDrawer(GravityCompat.START); return true
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (drawerLayoutUi != null) {
            drawerLayoutUi?.let { it.closeDrawer(GravityCompat.START) }
            return true
        }
        return false
    }
    
    /**
     * startIntroAnimation()将只被调用一次,
     * @see {@link pendingIntroAnimation}
     */
    protected open fun startIntroAnimation() {
        fabUi?.translationY = 2f * resources.getDimensionPixelOffset(R.dimen.btn_fab_size)
        
        if (toolbarUi != null) {
            var titleView: TextView? = null
            
            toolbarUi?.let {
                val actionbarSize = Utils.dpToPx(56)
                
                it.translationY = (-actionbarSize).toFloat()
                
                // getInboxMenuItem().getActionView().setTranslationY(-actionbarSize)
                
                for (i in 0 until it.childCount) {
                    val v = it.getChildAt(i)
                    if (v is TextView) {
                        titleView = v
                        v.setTranslationY((-actionbarSize).toFloat())
                        break
                    }
                }
                
                it.animate()
                    .translationY(0f)
                    .setDuration(ANIM_DURATION_TOOLBAR)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            postIntroAnimation()
                            startContentAnimation()
                        }
                    })
                    .startDelay = 300L
            }
            
            titleView?.let {
                it.animate()
                    .translationY(0f)
                    .setDuration(ANIM_DURATION_TOOLBAR)
                    .startDelay = 400L
            }
            //Timber.d("Toolbar textView animation: i=$i, v: $v")
        } else {
            postIntroAnimation()
        }
        
        // getInboxMenuItem().getActionView().animate()
        //         .translationY(0)
        //         .setDuration(ANIM_DURATION_TOOLBAR)
        //         .setStartDelay(500)
        //         .setListener(object : AnimatorListenerAdapter() {
        //             override fun onAnimationEnd(animation: Animator) {
        //                 startContentAnimation()
        //             }
        //         })
        //         .start()
    }
    
    
    protected open fun startContentAnimation() {
        if (fabUi != null) {
            fabUi?.let {
                it.animate()
                    .translationY(0f)
                    .setInterpolator(OvershootInterpolator(1f))
                    .setStartDelay(300)
                    .setDuration(ANIM_DURATION_FAB)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            postContentAnimation()
                        }
                    })
                    .start()
            }
        } else {
            postContentAnimation()
        }
    }
    
    protected open fun postIntroAnimation() {
        // add the once initializations
    }
    
    protected open fun postContentAnimation() {
        // add the once initializations
    }
    
    protected override fun postSetContentView(layoutResID: Int) {
        // add the once initializations
    }
    
    protected open val drawerLayoutUi: androidx.drawerlayout.widget.DrawerLayout?
        get() {
            return null
        }
    
    protected open val navDrawerUi: NavigationView?
        get() {
            return null
        }
    
    protected open fun setupAppBar() {
        //
    }
    
    protected open fun setupNavDrawer() {
        //
    }
    
    protected open fun setupTabs() {
        //
    }
    
    protected open fun setupViewPager() {
    
    }
    
    protected open fun setupBottomSheetView() {
        //
    }
    
    protected open fun setupBottomNav() {
        //
    }
    
    protected open val fabUi: FloatingActionButton?
        get() {
            return null
        }
    
    protected open fun setupFab() {
        //
    }
    
    protected open val toolbarUi: Toolbar?
        get() {
            return null
        }
    
    protected open fun setupToolbar() {
        if (toolbarUi != null) {
            toolbarUi?.let {
                setSupportActionBar(it)
                it.setNavigationIcon(R.drawable.ic_menu_white)
            }
        }
    }
    
    protected open fun afterSetupToolbar() {
        //
    }
    
}
