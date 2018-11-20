package com.obsez.mobile.meijue.ui.base

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.obsez.mobile.meijue.ui.R
import com.obsez.mobile.meijue.ui.base.fr.Entering
import com.obsez.mobile.meijue.ui.base.fr.FrameElements
import com.obsez.mobile.meijue.ui.ext.snackBar
import com.obsez.mobile.meijue.ui.pref.PreferenceUtil
import timber.log.Timber
import java.lang.ref.WeakReference

@Suppress("MemberVisibilityCanBePrivate")
open class BaseActivity : AppCompatActivity(), FrameElements {
    
    /**
     * 当回退栈仅剩一个fragment时，是否立即退出app
     */
    open var allowExitAtLastFragment: Boolean = false
    /**
     * 双击返回按钮才会退出app
     */
    open var allowDoubleTapToExit: Boolean = false
    
    
    override val drawerLayoutUi: DrawerLayout?
        get() = null
    override val navDrawerUi: NavigationView?
        get() = null
    override val actionBarDrawerToggleUi: ActionBarDrawerToggle?
        get() = null
    
    override val toolbarUi: Toolbar?
        get() = null
    override val collapsingToolbarLayoutUi: CollapsingToolbarLayout?
        get() = null
    
    override val tabLayoutUi: TabLayout?
        get() = null
    override val viewPagerUi: ViewPager?
        get() = null
    
    override val bottomNavigationViewUi: BottomNavigationView?
        get() = null
    override val fabUi: FloatingActionButton?
        get() = null
    
    //fun getSupportFragmentManager(): FragmentManager {
    //    return mFragments.supportFragmentManager
    //}
    
    protected var lastAttachedFragment: WeakReference<Fragment>? = null
    
    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        
        Timber.d("onAttachFragment: $fragment")
        
        if (fragment != null) {
            lastAttachedFragment = WeakReference(fragment)
            
            if (fragment is Entering) {
                fragment.onAttachToActivity(this)
            }
        }
    }
    
    
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        preOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState, persistentState)
        myCreate(savedInstanceState)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        preOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        myCreate(savedInstanceState)
    }
    
    protected open fun preOnCreate(savedInstanceState: Bundle?) {
        loadNightMode()
    }
    
    protected open fun myCreate(savedInstanceState: Bundle?) {
    }
    
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        postSetContentView(layoutResID)
    }
    
    fun superSetContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }
    
    protected open fun postSetContentView(layoutResID: Int) {
    
    }
    
    protected open fun loadNightMode() {
        val nm = nightMode
        if (nm != AppCompatDelegate.getDefaultNightMode()) {
            setNightMode(nm)
        }
    }
    
    @AppCompatDelegate.NightMode
    open var nightMode: Int
        get() {
            //return pref("nightMode", AppCompatDelegate.getDefaultNightMode()).getValue()
            val pref = PreferenceUtil.get(this)
            @Suppress("UnnecessaryVariable")
            @AppCompatDelegate.NightMode val nightModeLocal = pref.getInt("nightMode", AppCompatDelegate.getDefaultNightMode())
            //Timber.d("load nightMode : $nightMode_")
            return nightModeLocal
        }
        @SuppressLint("ObsoleteSdkInt")
        set(value) {
            setNightMode(value, true)
        }
    
    @SuppressLint("ObsoleteSdkInt")
    open fun setNightMode(@AppCompatDelegate.NightMode nightMode_: Int, needRecreate: Boolean = false) {
        val pref = PreferenceUtil.get(this)
        //pref.getInt("nightMode")
        pref.edit().putInt("nightMode", nightMode_).apply()
        //Timber.d("setNightMode : $value")
        AppCompatDelegate.setDefaultNightMode(nightMode_)
        
        if (Build.VERSION.SDK_INT >= 11 && needRecreate) {
            recreate()
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            //R.id.menu_night_mode_system -> { nightMode = (AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); true }
            //R.id.menu_night_mode_day -> { nightMode = (AppCompatDelegate.MODE_NIGHT_NO); true }
            //R.id.menu_night_mode_night -> { nightMode = (AppCompatDelegate.MODE_NIGHT_YES); true }
            //R.id.menu_night_mode_auto -> { nightMode = (AppCompatDelegate.MODE_NIGHT_AUTO); true }
            android.R.id.home -> {
                //mDrawerLayout.openDrawer(GravityCompat.START); return true
                onBackPressed()
                true
            }
            //R.id.action_settings -> true
            //R.id.action_favorites -> { startActivity<ScrollingActivity>(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    
    //
    
    
    fun fixBackStack(): Fragment? {
        val af = supportFragmentManager.activeFragment
        if (af != null) {
            val ft = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ft.show(af)
            ft.commitAllowingStateLoss()
        }
        return af
    }
    
    /**
     *
     * Activity + 多Frament 使用时的一些坑.md
     * https://github.com/gpfduoduo/android-article/blob/master/Activity%20%2B%20%E5%A4%9AFrament%20%E4%BD%BF%E7%94%A8%E6%97%B6%E7%9A%84%E4%B8%80%E4%BA%9B%E5%9D%91.md
     *
     */
    
    inline val FragmentManager.activeFragment: Fragment?
        get() {
            if (this.backStackEntryCount > 0) {
                val entry = this.getBackStackEntryAt(this.backStackEntryCount - 1)
                return this.findFragmentByTag(entry.name)
            }
            return null
        }
    
    
    fun superOnBackPressed() {
        super.onBackPressed()
    }
    
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        //super.onBackPressed()
        
        Timber.d("onBackPressed()")
        
        val fragmentManager = this.supportFragmentManager
        val isStateSaved = fragmentManager.isStateSaved
        // if (isStateSaved && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
        //     // Older versions will throw an exception from the framework
        //     // FragmentManager.popBackStackImmediate(), so we'll just
        //     // return here. The Activity is likely already on its way out
        //     // since the fragmentManager has already been saved.
        //     return
        // }
        if (!isStateSaved || Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if (allowExitAtLastFragment) {
                // 当只有初始fragment时，不允许弹出backStack，而是应该退出app了
                if (fragmentManager.backStackEntryCount > 1) {
                    this.doubleBackToExitPressedOnce = false
                    superOnBackPressed()
                    fixBackStack()
                    return
                }
            }
        }
        
        
        if (allowDoubleTapToExit) {
            if (doubleBackToExitPressedOnce) {
                if (fragmentManager.backStackEntryCount == 1) {
                    fragmentManager.popBackStack() // 弹出初始fragment
                }
                superOnBackPressed()
                this.doubleBackToExitPressedOnce = false
                return
            }
            
            this.doubleBackToExitPressedOnce = true
            snackBar(getString(R.string.twice_back_pressed_to_exit))
            
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else
            superOnBackPressed()
    }
    
    
}
