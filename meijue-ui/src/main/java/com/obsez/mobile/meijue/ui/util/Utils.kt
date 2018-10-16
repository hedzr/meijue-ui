package com.obsez.mobile.meijue.ui.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.obsez.mobile.meijue.ui.MeijueUiAppModule
import timber.log.Timber


//////////////////

object Utils {
    private var screenWidth = 0
    private var screenHeight = 0
    
    val isAndroid5: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    
    fun dp2px(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
    
    fun dp2px(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }
    
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
    
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
    
    fun px2dp(pxValue: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt() // + 0.5f是为了让结果四舍五入
    }
    
    fun px2dp(pxValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale) // + 0.5f是为了让结果四舍五入
    }
    
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt() // + 0.5f是为了让结果四舍五入
    }
    
    fun px2dp_(context: Context, pxValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, context.resources.displayMetrics).toInt()
    }
    
    fun px2sp_(context: Context, pxValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pxValue, context.resources.displayMetrics).toInt()
    }
    
    fun px2sp(pxValue: Int): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
    
    fun px2sp(pxValue: Float): Float {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / fontScale)
    }
    
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
    
    fun sp2px(sp: Int): Int {
        return (sp.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }
    
    fun sp2px(sp: Float): Float {
        return sp * Resources.getSystem().displayMetrics.scaledDensity
    }
    
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
    
    
    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }
        
        return screenHeight
    }
    
    fun getScreenWidth(c: Context): Int {
        if (screenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }
        
        return screenWidth
    }
    
    /**
     * https://developer.android.com/training/multiscreen/screensizes#TaskUseSWQuali
     *
     * <code>
     *     if (BuildConfig.DEBUG) Utils.checkAndLogScreenInfo(mainActivity)
     * </code>
     */
    fun checkAndLogScreenInfo(activity: Activity) {
        val display = activity.windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        
        val wInches = displayMetrics.widthPixels / (displayMetrics.densityDpi.toDouble())
        val hInches = displayMetrics.heightPixels / (displayMetrics.densityDpi.toDouble())
        
        val screenDiagonal = Math.sqrt(Math.pow(wInches, 2.0) + Math.pow(hInches, 2.0))
        val isTablet = screenDiagonal >= 7.0
        
        val wDp = this.px2dp(displayMetrics.widthPixels)
        val hDp = this.px2dp(displayMetrics.heightPixels)
        
        Timber.v("isTablet: $isTablet, screen: ${displayMetrics.widthPixels}x${displayMetrics.heightPixels}, dp: ${wDp}x$hDp, inch: ${wInches}x$hInches")
    }
    
    
    fun checkAndLogStorageInfo(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            @Suppress("LocalVariableName")
            val PERMISSION_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE = 0
            
            MeijueUiAppModule.get().context.let {
                val readPermissionCheck = ContextCompat.checkSelfPermission(it,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                val writePermissionCheck = ContextCompat.checkSelfPermission(it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                
                //if = permission granted
                if (readPermissionCheck == PackageManager.PERMISSION_GRANTED && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
                    llCheckStorage()
                } else {
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE)
                    
                    val readPermissionCheck2 = ContextCompat.checkSelfPermission(it,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                    val writePermissionCheck2 = ContextCompat.checkSelfPermission(it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (readPermissionCheck2 == PackageManager.PERMISSION_GRANTED && writePermissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                        llCheckStorage()
                    }
                    return
                }
            }
        }
    }
    
    @SuppressLint("ServiceCast")
    private fun llCheckStorage() {
        MeijueUiAppModule.get().context.let {
            val a = FileUtil.getStoragePath(it, false)
            val b = FileUtil.getStoragePath(it, true)
            Timber.v("removable: $b, internal: $a")
            val c = Environment.getExternalStorageDirectory().absolutePath
            Timber.v("env.external/primary : $c")
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //StorageManager
                val sm = it.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                for (sv in sm.storageVolumes) {
                    Timber.v("- storageVolume: $sv")
                }
            }
        }
    }
    
}
