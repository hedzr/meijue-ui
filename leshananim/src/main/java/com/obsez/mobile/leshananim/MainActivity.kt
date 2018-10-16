package com.obsez.mobile.leshananim

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.obsez.mobile.leshananim.ui.login.LoginActivity
import com.obsez.mobile.leshananim.ui.login.LoginBottomSheetDialogFragment
import com.obsez.mobile.leshananim.ui.settings.SettingsActivity
import com.obsez.mobile.meijue.ui.activity.ToolbarAnimActivity
import com.obsez.mobile.meijue.ui.ext.snackBar
import com.obsez.mobile.meijue.ui.ext.startActivity
import com.obsez.mobile.meijue.ui.receivers.HandsetPlugDetectReceiver
import com.obsez.mobile.meijue.ui.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber

class MainActivity : ToolbarAnimActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    override val fabUi: FloatingActionButton? by lazy { fab }
    
    override val toolbarUi: Toolbar? by lazy { toolbar }
    
    override val navDrawerUi: NavigationView? by lazy { nav_view }
    
    override val drawerLayoutUi: androidx.drawerlayout.widget.DrawerLayout? by lazy { drawer_layout }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
        
        //        fab.setOnClickListener { view ->
        //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                    .setAction("Action", null).show()
        //        }
        //
        //        val toggle = ActionBarDrawerToggle(
        //                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //        drawer_layout.addDrawerListener(toggle)
        //        toggle.syncState()
        //
        //        nav_view.setNavigationItemSelectedListener(this)
        
        if (BuildConfig.DEBUG) {
            Utils.checkAndLogStorageInfo(this)
            Utils.checkAndLogScreenInfo(this)
        }
        
        checkHandsetStatus()
    }
    
    override fun postContentAnimation() {
        super.postContentAnimation()
        
        btn_notify.setOnClickListener {
            NotifyHelper.notify123(this)
        }
        
        btn_notify_2.setOnClickListener {
            NotifyHelper.notify456(this)
        }
        
        btn_notify_3.setOnClickListener {
            NotifyHelper.notify789(this)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        HandsetPlugDetectReceiver.unregister(this)
    }
    
    
    override fun setupFab() {
        fab.setOnClickListener {
            //view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            snackBar("Replace with your own action", Snackbar.LENGTH_LONG) {
                setAction("Action", null)
            }
        }
    }
    
    override fun setupNavDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        
        nav_view.setNavigationItemSelectedListener(this)
    }
    
    //    override fun onBackPressed() {
    //        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
    //            drawer_layout.closeDrawer(GravityCompat.START)
    //        } else {
    //            super.onBackPressed()
    //        }
    //    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //val pref = PreferenceUtil.get(this)
        //@AppCompatDelegate.NightMode val nightMode = pref.getInt("nightMode", AppCompatDelegate.getDefaultNightMode())
        //Timber.d("load nightMode : $nightMode")
        when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> menu.findItem(R.id.menu_night_mode_system)?.isChecked = true
            // 22:00 - 07:00 时间段内自动切换为夜间模式
            // 可以有机会拦截这个判定，改为实现自己的机制，例如依据日出日落（需要地理坐标）来变更。
            AppCompatDelegate.MODE_NIGHT_AUTO -> menu.findItem(R.id.menu_night_mode_auto)?.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> menu.findItem(R.id.menu_night_mode_night)?.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> menu.findItem(R.id.menu_night_mode_day)?.isChecked = true
        }
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_night_mode_system -> {
                nightMode = (AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); true
            }
            R.id.menu_night_mode_day -> {
                nightMode = (AppCompatDelegate.MODE_NIGHT_NO); true
            }
            R.id.menu_night_mode_night -> {
                nightMode = (AppCompatDelegate.MODE_NIGHT_YES); true
            }
            R.id.menu_night_mode_auto -> {
                nightMode = (AppCompatDelegate.MODE_NIGHT_AUTO); true
            }
            
            // android.R.id.home -> { mDrawerLayout.openDrawer(GravityCompat.START); return true }
            
            R.id.action_settings -> {
                //startActivity<ScrollingActivity>()
                startActivity<SettingsActivity>()
                true
            }
            
            R.id.action_login -> return showLogin()
            R.id.action_login_bs -> return showLoginBottomSheetDialog()
            R.id.action_login_page -> {
                startActivity<LoginActivity>()
                true
            }
            
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
                
                // ext.startActivity
                startActivity<MainBnvActivity>()
            }
            R.id.nav_gallery -> {
                startActivity<SettingsActivity>()
            }
            R.id.nav_slideshow -> {
                startActivity<LoginActivity>()
            }
            R.id.nav_manage -> {
            
            }
            R.id.nav_share -> {
            
            }
            R.id.nav_send -> {
            
            }
        }
        
        return super.onNavigationItemSelected(item)
        //drawer_layout.closeDrawer(GravityCompat.START)
        //return true
    }
    
    
    private fun checkHandsetStatus() {
        HandsetPlugDetectReceiver.register(this) {
            snackBar("headset present: $it (recheck: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}), bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}", Snackbar.LENGTH_INDEFINITE)
        }
        Timber.v("headset present: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}")
        Timber.v("bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}")
        snackBar("headset present: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}, bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}", Snackbar.LENGTH_INDEFINITE)
    }
    
    private fun showLogin(): Boolean {
        val dlg = LoginBottomSheetDialogFragment()
        dlg.show(supportFragmentManager, null)
        return true
    }
    
    private fun showLoginBottomSheetDialog(): Boolean {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.login_bottom_sheet_dialog, null)
        dialog.setContentView(view)
        dialog.show()
        return true
    }
    
}

