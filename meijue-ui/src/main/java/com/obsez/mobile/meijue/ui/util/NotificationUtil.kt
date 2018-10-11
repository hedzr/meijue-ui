package com.obsez.mobile.meijue.ui.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.obsez.mobile.meijue.ui.MeijueUiAppModule
import com.obsez.mobile.meijue.ui.R
import java.lang.ref.WeakReference


/**
 *
 */
class NotificationUtil private constructor() {
    
    companion object {
        val instance by lazy { NotificationUtil() }
    }
    
    private val mContext: WeakReference<Context>
    private var mNotificationManager: NotificationManager
    
    private val mChannels = mutableMapOf<String, Boolean>()
    private var mLastChannelId: String = ""
    private var mLastNotificationId = -1
    private val mNotifications = mutableMapOf<Int, Notification>()
    private val mNotificationBuilders = mutableMapOf<Int, NotificationCompat.Builder>()
    private var mLastSender: Person? = null
    
    init {
        //mContext = WeakReference<Context>(null)
        //from(MeijueUiAppModule.get().context)
        val context = MeijueUiAppModule.get().context
        mContext = WeakReference(context)
        mNotificationManager = context.applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    
    val notificationManager: NotificationManager
        get() {
            return mNotificationManager
        }
    
    /**
     *
     * <code>
     * .channel(id, desc) {
     *     setDescription(description);
     *     enableLights(true);
     *     setLightColor(Color.RED);
     *     enableVibration(true);
     *     setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
     * }
     * </code>
     */
    fun channel(id: String, desc: String? = null, importance: Int = 3, func: (NotificationChannel.() -> Unit)? = null): NotificationUtil {
        mLastChannelId = id
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!mChannels.containsKey(id)) {
                mNotificationManager?.let {
                    val imp = when (importance) {
                        0 -> NotificationManager.IMPORTANCE_NONE
                        1 -> NotificationManager.IMPORTANCE_MIN
                        2 -> NotificationManager.IMPORTANCE_LOW
                        3 -> NotificationManager.IMPORTANCE_DEFAULT
                        4 -> NotificationManager.IMPORTANCE_MAX
                        else -> {
                            TODO("`importance` is not valid integer in 0..4")
                        }
                    }
                    val mChannel = NotificationChannel(id, desc, imp)
                    if (func != null) {
                        mChannel.func()
                    }
                    it.createNotificationChannel(mChannel)
                    mChannels[id] = true
                }
            }
        }
        return this
    }
    
    /**
     *
     * <code>
     *     .builder(idNotification) {
     *         setSmallIcon(...)
     *         setContentTitle(...)
     *         setContentText(...)
     *         setPriority(...)
     *     }
     * </code>
     *
     * if you wanna invoke @{link #sender()}
     * @param cacheAtFirst true means the notification builder will be cached, false means the notification will be built right now
     *
     */
    fun builder(id: Int,
                title: CharSequence? = null,
                text: CharSequence? = null,
                @DrawableRes smallIcon: Int = -1,
                priority: Int = -1,
                subText: CharSequence? = null,
                cacheAtFirst: Boolean = true,
                func: (NotificationCompat.Builder.() -> Unit)): NotificationUtil {
        mContext.get()?.let {
            val builder = NotificationCompat.Builder(it, mLastChannelId)
            if (smallIcon != -1) builder.setSmallIcon(smallIcon)
            if (title != null) builder.setContentTitle(title)
            if (text != null) builder.setContentText(text)
            if (subText != null) builder.setSubText(subText)
            if (priority != -1) builder.priority = priority
            builder.func()
            mLastNotificationId = id
            if (cacheAtFirst) {
                mNotificationBuilders[id] = builder
            } else {
                mNotifications[id] = builder.build()
            }
        }
        return this
    }
    
    fun sender(name: String? = null,
               icon: IconCompat? = null,
               func: (Person.Builder.() -> Person.Builder)): NotificationUtil {
        mContext.get()?.let {
            if (mNotificationBuilders.containsKey(mLastNotificationId)) {
                
                mLastSender = Person.Builder()
                    .setName(name ?: it.getString(R.string.default_user_name))
                    .setIcon(icon ?: IconCompat.createWithResource(it, R.drawable.ic_person_black_24dp))
                    .func()
                    .build()
            }
        }
        return this
    }
    
    val defaultSenderBuilder by lazy {
        mContext.get()!!.let {
            Person.Builder()
                .setName(it.getString(R.string.default_user_name))
                .setIcon(IconCompat.createWithResource(it, R.drawable.ic_person_black_24dp))
        }
    }
    
    
    /**
     * add messages from sender, see also @{link NotificationCompat.MessagingStyle}:
     *
     * <code>
     *  .messagingStyle {
     *      addMessage("Check this out!", Date().time - 180,
     *          NotificationUtil.instance.defaultSenderBuilder.setBot(true).build())
     *      addMessage("okay, got it.", Date().time,
     *          NotificationUtil.instance.defaultSenderBuilder
     *              .setName("Jasper Brown")
     *              .setIcon(IconCompat.createWithResource(parentActivity,
     *                  android.R.drawable.ic_dialog_email))
     *              .setBot(false).build())
     *
     *      setGroupConversation(true)
     *  }
     * </code>
     */
    fun messagingStyle(func: (NotificationCompat.MessagingStyle.() -> NotificationCompat.MessagingStyle)): NotificationUtil {
        mContext.get()?.let {
            if (mNotificationBuilders.containsKey(mLastNotificationId) && mLastSender != null) {
                NotificationCompat.MessagingStyle(mLastSender!!)
                    .func()
                    .setBuilder(mNotificationBuilders[mLastNotificationId])
            }
        }
        return this
    }
    
    fun action(icon: Int,
               title: CharSequence,
               intent: PendingIntent,
               @NotificationCompat.Action.SemanticAction semanticAction: Int = NotificationCompat.Action.SEMANTIC_ACTION_NONE,
               func: (NotificationCompat.Action.Builder.() -> NotificationCompat.Action.Builder)? = null): NotificationUtil {
        mContext.get()?.let {
            if (mNotificationBuilders.containsKey(mLastNotificationId) && mLastSender != null) {
                val builder = NotificationCompat.Action.Builder(icon, title, intent)
                    .setSemanticAction(semanticAction)
                if (func != null) builder.func()
                mNotificationBuilders[mLastNotificationId]!!.addAction(builder.build())
            }
        }
        return this
    }
    
    fun show(tag: String? = null) {
        mContext.get()?.let {
            if (mLastNotificationId > 0) {
                if (!mNotifications.containsKey(mLastNotificationId)) {
                    if (mNotificationBuilders.containsKey(mLastNotificationId)) {
                        mNotifications[mLastNotificationId] = mNotificationBuilders[mLastNotificationId]!!.build()
                    }
                }
                NotificationManagerCompat.from(it).notify(tag, mLastNotificationId, mNotifications[mLastNotificationId]!!)
            }
        }
    }
    
    fun cancelAll() {
        mContext.get()?.let {
            mNotificationManager?.cancelAll()
        }
    }
    
    fun cancel(id: Int) {
        mContext.get()?.let {
            mNotificationManager?.cancel(id)
        }
    }
    
    fun cancel(tag: String, id: Int) {
        mContext.get()?.let {
            mNotificationManager?.cancel(tag, id)
        }
    }
    
    
}
