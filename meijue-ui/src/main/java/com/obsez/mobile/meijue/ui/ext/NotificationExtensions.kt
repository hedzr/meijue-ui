package com.obsez.mobile.meijue.ui.ext


import android.app.Notification
import android.content.Context
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat

@Suppress("NOTHING_TO_INLINE")
inline fun Notification.build(@NonNull context: Context, noinline func: (NotificationCompat.Builder.() -> Unit)? = null): Notification {
    val builder = NotificationCompat.Builder(context)
    func?.let { builder.it() }
    return builder.build()
}
