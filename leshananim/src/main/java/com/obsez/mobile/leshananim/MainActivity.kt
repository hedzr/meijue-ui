package com.obsez.mobile.leshananim

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.obsez.mobile.leshananim.ui.login.LoginActivity
import com.obsez.mobile.leshananim.ui.login.LoginBottomSheetDialogFragment
import com.obsez.mobile.leshananim.ui.settings.SettingsActivity
import com.obsez.mobile.meijue.ui.ToolbarAnimActivity
import com.obsez.mobile.meijue.ui.ext.snackBar
import com.obsez.mobile.meijue.ui.ext.startActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarAnimActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    override val fabUi: FloatingActionButton? by lazy { fab }
    
    override val toolbarUi: Toolbar? by lazy { toolbar }
    
    override val navDrawerUi: NavigationView? by lazy { nav_view }
    
    override val drawerLayoutUi: DrawerLayout? by lazy { drawer_layout }
    
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
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_login -> return showLogin() //showLoginBottomSheetDialog()
            else -> super.onOptionsItemSelected(item)
        }
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
            
            }
            R.id.nav_manage -> {
                startActivity<LoginActivity>()
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
}
