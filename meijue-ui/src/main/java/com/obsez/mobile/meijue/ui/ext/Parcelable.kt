package com.obsez.mobile.meijue.ui.ext


//fun Parcelable.dp2px(dp: Int): Int {
//    return (dp * Resources.getSystem().displayMetrics.density).toInt()
//}
//
//fun Parcelable.dp2px(dp: Float): Float {
//    return dp * Resources.getSystem().displayMetrics.density
//}
//
//fun Parcelable.dp2px(context: Context, dpValue: Float): Int {
//    val scale = context.resources.displayMetrics.density
//    return (dpValue * scale + 0.5f).toInt()
//}
//
//fun Parcelable.dpToPx(dp: Int): Int {
//    return (dp * Resources.getSystem().displayMetrics.density).toInt()
//}
//
//fun Parcelable.px2dp(pxValue: Int): Int {
//    val scale = Resources.getSystem().displayMetrics.density
//    return (pxValue / scale + 0.5f).toInt() // + 0.5f是为了让结果四舍五入
//}
//
//fun Parcelable.px2dp(pxValue: Float): Float {
//    val scale = Resources.getSystem().displayMetrics.density
//    return (pxValue / scale) // + 0.5f是为了让结果四舍五入
//}
//
//fun Parcelable.px2dp(context: Context, pxValue: Float): Int {
//    val scale = context.resources.displayMetrics.density
//    return (pxValue / scale + 0.5f).toInt() // + 0.5f是为了让结果四舍五入
//}
//
//fun Parcelable.px2dp_(context: Context, pxValue: Float): Int {
//    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, context.resources.displayMetrics).toInt()
//}
//
//fun Parcelable.px2sp_(context: Context, pxValue: Float): Int {
//    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pxValue, context.resources.displayMetrics).toInt()
//}
//
//fun Parcelable.px2sp(pxValue: Int): Int {
//    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
//    return (pxValue / fontScale + 0.5f).toInt()
//}
//
//fun Parcelable.px2sp(pxValue: Float): Float {
//    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
//    return (pxValue / fontScale)
//}
//
//fun Parcelable.px2sp(context: Context, pxValue: Float): Int {
//    val fontScale = context.resources.displayMetrics.scaledDensity
//    return (pxValue / fontScale + 0.5f).toInt()
//}
//
//fun Parcelable.sp2px(sp: Int): Int {
//    return (sp.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()
//}
//
//fun Parcelable.sp2px(sp: Float): Float {
//    return sp * Resources.getSystem().displayMetrics.scaledDensity
//}
//
//fun Parcelable.sp2px(context: Context, spValue: Float): Int {
//    val fontScale = context.resources.displayMetrics.scaledDensity
//    return (spValue * fontScale + 0.5f).toInt()
//}
