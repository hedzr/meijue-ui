package com.obsez.mobile.meijue.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.obsez.mobile.meijue.ui.pref.PreferenceUtil

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        preOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState, persistentState)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        preOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        myCreate(savedInstanceState)
    }
    
    protected open fun preOnCreate(savedInstanceState: Bundle?) {
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
    
    fun loadNightMode() {
        val nm = nightMode
        if (nm != AppCompatDelegate.getDefaultNightMode()) {
            setNightMode(nm)
        }
    }
    
    @AppCompatDelegate.NightMode
    var nightMode: Int
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
    fun setNightMode(@AppCompatDelegate.NightMode nightMode_: Int, needRecreate: Boolean = false) {
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
    
}
