package com.obsez.mobile.leshananim

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.obsez.mobile.leshananim.ui.login.LoginActivity
import com.obsez.mobile.meijue.ui.util.NotificationUtil
import java.util.*


@Suppress("LocalVariableName")
object NotifyHelper {
    
    
    private var idNotification = 1
    
    
    fun notify123(activity: Activity) {
        val idChannel = "channel_1"
        val description = "123"
        
        val contentN = "who am i"
        val descN = "this is a details"
        
        val intent1 = Intent(activity, LoginActivity::class.java)
        val pi = PendingIntent.getActivity(activity, 0, intent1, 0);
        
        
        val jackie = NotificationUtil.instance.defaultSenderBuilder.setBot(true).build()
        val jasper = NotificationUtil.instance.defaultSenderBuilder
            .setName("Jasper Brown")
            .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_dashboard_black_24dp))
            .setBot(false).build()
        
        
        NotificationUtil.instance
            //.from(this)
            .channel(idChannel, description)
            .builder(idNotification++, contentN, descN) {
                setSmallIcon(R.mipmap.ic_launcher)
                setLargeIcon(BitmapFactory.decodeResource(activity.resources, R.drawable.ic_home_black_24dp))
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
    
    
    fun notify456(activity: Activity) {
        val idChannel = "channel_2"
        val description = "456"
        
        val contentN = "who am i 2"
        val descN = "this is a details 2"
        
        val intent1 = Intent(activity, LoginActivity::class.java)
        val pi = PendingIntent.getActivity(activity, 0, intent1, 0);
        
        
        val jackie = NotificationUtil.instance.defaultSenderBuilder.setBot(true).build()
        val jasper = NotificationUtil.instance.defaultSenderBuilder
            .setName("Jasper Brown")
            .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_dashboard_black_24dp))
            .setBot(false).build()
        
        
        NotificationUtil.instance
            //.from(this)
            .channel(idChannel, description)
            .builder(idNotification++, contentN, descN) {
                setSmallIcon(R.mipmap.ic_launcher)
                setLargeIcon(BitmapFactory.decodeResource(activity.resources, R.drawable.ic_home_black_24dp))
                
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
                setConversationTitle("Bond®")
                //
            }
            .action(R.drawable.ic_sync_black_24dp, "Process them", pi,
                NotificationCompat.Action.SEMANTIC_ACTION_DELETE)
            .show()
    }
    
    
    fun notify789(activity: Activity) {
        val idChannel = "channel_3"
        val description = "789"
        
        val contentN = "who am i 3"
        val descN = "this is a details 3"
        
        val intent1 = Intent(activity, LoginActivity::class.java)
        val pi = PendingIntent.getActivity(activity, 0, intent1, 0);
        
        
        val jackie = NotificationUtil.instance.defaultSenderBuilder.setBot(true).build()
        val jasper = NotificationUtil.instance.defaultSenderBuilder
            .setName("Jasper Brown")
            .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_dashboard_black_24dp))
            .setBot(false)
            .build()
        val james = NotificationUtil.instance.defaultSenderBuilder
            .setName("James Bond")
            .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_person_black_24dp))
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
                setLargeIcon(BitmapFactory.decodeResource(activity.resources, R.drawable.ic_person_outline_black_24dp))
                
                // auto cancel the notification with contentIntent
                setAutoCancel(true)
                setContentIntent(pi)
                
                setGroup(GROUP_KEY_WORK_EMAIL)
            }
            .builder(idNotification++, contentN, descN) {
                setSmallIcon(R.drawable.ic_person_black_24dp)
                setLargeIcon(BitmapFactory.decodeResource(activity.resources, R.drawable.ic_person_black_24dp))
                
                // auto cancel the notification with contentIntent
                setAutoCancel(true)
                setContentIntent(pi)
                
                setGroup(GROUP_KEY_WORK_EMAIL)
            }
            .builder(idNotification++, contentN, descN) {
                setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                setLargeIcon(BitmapFactory.decodeResource(activity.resources, R.drawable.ic_dashboard_black_24dp))
                
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
            //.sender(james)
            //.messagingStyle {
            //    addMessage("Check this out!", Date().time - 600000, jackie)
            //    addMessage("r u sure?", Date().time - 180000, jackie)
            //    addMessage("okay, got it.", Date().time - 60000, jasper)
            //    addMessage("sounds good", Date().time, jackie)
            //    setGroupConversation(true)
            //    setConversationTitle("007®")
            //    //
            //}
            .action(R.drawable.ic_notifications_paused_black_24dp, "Process them", pi,
                NotificationCompat.Action.SEMANTIC_ACTION_DELETE)
            .action(R.drawable.ic_notifications_paused_black_24dp, "Call", pi,
                NotificationCompat.Action.SEMANTIC_ACTION_CALL)
            .action(R.drawable.ic_notifications_paused_black_24dp, "Reply", pi,
                NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
            .show()
    }
    
    
}

