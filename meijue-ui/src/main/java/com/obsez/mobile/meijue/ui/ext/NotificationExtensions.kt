package com.obsez.mobile.meijue.ui.ext


import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationCompat

inline fun Notification.build(context: Context, func: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(context)
    builder.func()
    return builder.build()
}
