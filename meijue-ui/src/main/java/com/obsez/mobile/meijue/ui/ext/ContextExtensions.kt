package com.obsez.mobile.meijue.ui.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.jetbrains.annotations.NotNull


fun Context.toast(@StringRes id: Int, vararg args: Any?, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, getString(id, args), duration).show()
fun Context.toast(@NotNull msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, duration).show()
//fun Context.snackBar(@StringRes id: Int, vararg args: Any?, duration: Int = Snackbar.LENGTH_LONG, func: Snackbar.() -> Snackbar) = Snackbar.make(this, getString(id, args), duration).func().show()
//fun Context.snackBar(@NotNull msg: CharSequence, duration: Int = Snackbar.LENGTH_LONG, func: Snackbar.() -> Snackbar) = Snackbar.make(this, msg, duration).func().show()


fun Context.dp2px(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun Context.dp2px(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}

//fun Context.dp2px(dpValue: Float): Int {
//    val scale = resources.displayMetrics.density
//    return (dpValue * scale + 0.5f).toInt()
//}

fun Context.dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun Context.px2dp(pxValue: Int): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (pxValue / scale + 0.5f).toInt() // + 0.5f是为了让结果四舍五入
}

fun Context.px2dp(pxValue: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return (pxValue / scale) // + 0.5f是为了让结果四舍五入
}

//fun Context.px2dp(context: Context, pxValue: Float): Int {
//    val scale = context.resources.displayMetrics.density
//    return (pxValue / scale + 0.5f).toInt() // + 0.5f是为了让结果四舍五入
//}

fun Context.px2dp_(pxValue: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, resources.displayMetrics).toInt()
}

fun Context.px2sp_(pxValue: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pxValue, resources.displayMetrics).toInt()
}

fun Context.px2sp(pxValue: Int): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Float {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (pxValue / fontScale)
}

//fun Context.px2sp(context: Context, pxValue: Float): Int {
//    val fontScale = context.resources.displayMetrics.scaledDensity
//    return (pxValue / fontScale + 0.5f).toInt()
//}

fun Context.sp2px(sp: Int): Int {
    return (sp.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()
}

fun Context.sp2px(sp: Float): Float {
    return sp * Resources.getSystem().displayMetrics.scaledDensity
}

//fun Context.sp2px(context: Context, spValue: Float): Int {
//    val fontScale = context.resources.displayMetrics.scaledDensity
//    return (spValue * fontScale + 0.5f).toInt()
//}


/**
 * Load as {@link Bitmap}, from resources/drawable.
 */
fun Context.fromDrawable(@DrawableRes drawableId: Int): Bitmap {
    return BitmapFactory.decodeResource(resources, drawableId)
}


fun Context.copyToClipboard(data: String, showToast: Boolean = true) {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("clipboard", data)
    clipboard.primaryClip = clip
    if (showToast)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
}




