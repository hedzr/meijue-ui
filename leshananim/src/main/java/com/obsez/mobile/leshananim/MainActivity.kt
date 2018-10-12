package com.obsez.mobile.leshananim

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
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
import com.obsez.mobile.meijue.ui.util.NotificationUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import java.util.*

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
        
        
    }
    
    var idNotification = 1
    
    override fun postContentAnimation() {
        super.postContentAnimation()
        
        checkHandsetStatus()
        
        btn_notify.setOnClickListener {
            val idChannel = "channel_1"
            val description = "123"
            
            val contentN = "who am i"
            val descN = "this is a details"
            
            val intent1 = Intent(this, LoginActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent1, 0);
            
            
            val jackie = NotificationUtil.instance.defaultSenderBuilder.setBot(true).build()
            val jasper = NotificationUtil.instance.defaultSenderBuilder
                .setName("Jasper Brown")
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_dashboard_black_24dp))
                .setBot(false).build()
            
            
            NotificationUtil.instance
                //.from(this)
                .channel(idChannel, description)
                .builder(idNotification++, contentN, descN) {
                    setSmallIcon(R.mipmap.ic_launcher)
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_home_black_24dp))
                    setContentIntent(pi)
                }
                .sender {
                    setBot(false)
                }
                .messagingStyle {
                    addMessage("Check this out!", Date().time - 600000, jackie)
                    addMessage("r u sure?", Date().time - 180000, jackie)
                    addMessage("okay, got it.", Date().time - 60000, jasper)
                    addMessage("sounds good", Date().time, jackie)
                    setConversationTitle("djsljdk®")
                    setGroupConversation(true)
                }
                .action(R.drawable.ic_sync_black_24dp, "Process them", pi,
                    NotificationCompat.Action.SEMANTIC_ACTION_MARK_AS_READ)
                .show()
        }
        
        btn_notify_2.setOnClickListener {
            val idChannel = "channel_2"
            val description = "456"
            
            val contentN = "who am i 2"
            val descN = "this is a details 2"
            
            val intent1 = Intent(this, LoginActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent1, 0);
            
            
            val jackie = NotificationUtil.instance.defaultSenderBuilder.setBot(true).build()
            val jasper = NotificationUtil.instance.defaultSenderBuilder
                .setName("Jasper Brown")
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_dashboard_black_24dp))
                .setBot(false).build()
            
            
            NotificationUtil.instance
                //.from(this)
                .channel(idChannel, description)
                .builder(idNotification++, contentN, descN) {
                    setSmallIcon(R.mipmap.ic_launcher)
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_home_black_24dp))
                    
                    // auto cancel the notification with contentIntent
                    setAutoCancel(true)
                    setContentIntent(pi)
                }
                .sender {
                    setBot(false)
                }
                .messagingStyle {
                    addMessage("Check this out!", Date().time - 600000, jackie)
                    addMessage("r u sure?", Date().time - 180000, jackie)
                    addMessage("okay, got it.", Date().time - 60000, jasper)
                    addMessage("sounds good", Date().time, jackie)
                    setGroupConversation(true)
                    setConversationTitle("djsljdk®")
                    //
                }
                .action(R.drawable.ic_sync_black_24dp, "Process them", pi,
                    NotificationCompat.Action.SEMANTIC_ACTION_DELETE)
                .show()
        }
        
        btn_notify_3.setOnClickListener {
            val idChannel = "channel_3"
            val description = "789"
            
            val contentN = "who am i 3"
            val descN = "this is a details 3"
            
            val intent1 = Intent(this, LoginActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent1, 0);
            
            
            val jackie = NotificationUtil.instance.defaultSenderBuilder.setBot(true).build()
            val jasper = NotificationUtil.instance.defaultSenderBuilder
                .setName("Jasper Brown")
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_dashboard_black_24dp))
                .setBot(false)
                .build()
            val james = NotificationUtil.instance.defaultSenderBuilder
                .setName("James Bond")
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_person_black_24dp))
                .setBot(false)
                .build()
            
            val contentM = "golang orm"
            val descM = "such as mysql, postgresql, oracle, sqlite, ...."
            val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"
            NotificationUtil.instance
                //.from(this)
                .channel(idChannel, description)
                .builder(idNotification++, contentM, descM) {
                    setSmallIcon(R.drawable.ic_person_outline_black_24dp)
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_person_outline_black_24dp))
                    
                    // auto cancel the notification with contentIntent
                    setAutoCancel(true)
                    setContentIntent(pi)
                    
                    setGroup(GROUP_KEY_WORK_EMAIL)
                }
                .builder(idNotification++, contentN, descN) {
                    setSmallIcon(R.drawable.ic_person_black_24dp)
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_person_black_24dp))
        
                    // auto cancel the notification with contentIntent
                    setAutoCancel(true)
                    setContentIntent(pi)
        
                    setGroup(GROUP_KEY_WORK_EMAIL)
                }
                .builder(idNotification++, contentN, descN) {
                    setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_dashboard_black_24dp))
        
                    // auto cancel the notification with contentIntent
                    setAutoCancel(true)
                    setContentIntent(pi)
        
                    setGroup(GROUP_KEY_WORK_EMAIL)
                    setGroupSummary(true)
                }
                .inboxStyle("2 new messages", "janedoe@example.com") {
                    addLine("Alex Faarborg Check this out")
                    addLine("Jeff Chang Launch Party")
                    setBigContentTitle("2 new messages")
                    setSummaryText("janedoe@example.com")
                }
//                .sender(james)
//                .messagingStyle {
//                    addMessage("Check this out!", Date().time - 600000, jackie)
//                    addMessage("r u sure?", Date().time - 180000, jackie)
//                    addMessage("okay, got it.", Date().time - 60000, jasper)
//                    addMessage("sounds good", Date().time, jackie)
//                    setGroupConversation(true)
//                    setConversationTitle("007®")
//                    //
//                }
                .action(R.drawable.ic_notifications_paused_black_24dp, "Process them", pi,
                    NotificationCompat.Action.SEMANTIC_ACTION_DELETE)
                .action(R.drawable.ic_notifications_paused_black_24dp, "Call", pi,
                    NotificationCompat.Action.SEMANTIC_ACTION_CALL)
                .action(R.drawable.ic_notifications_paused_black_24dp, "Reply", pi,
                    NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
                .show()
            
        }
    }
    
    private fun checkHandsetStatus() {
        HandsetPlugDetectReceiver.register(this) {
            snackBar("headset present: $it (recheck: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}), bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}", Snackbar.LENGTH_INDEFINITE)
        }
        Timber.v("headset present: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}")
        Timber.v("bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}")
        snackBar("headset present: ${HandsetPlugDetectReceiver.getHeadsetStatus(this)}, bluetooth headset present: ${HandsetPlugDetectReceiver.getBlootoothHeadsetStatus(this)}", Snackbar.LENGTH_INDEFINITE)
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
