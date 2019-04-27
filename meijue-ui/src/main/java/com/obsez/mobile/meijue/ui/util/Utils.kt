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
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.obsez.mobile.meijue.ui.MeijueUi
import timber.log.Timber


//////////////////

object Utils {
    
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
    
    
    @Px
    fun getScreenHeight(c: Context): Int {
        if (mScreenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            mScreenHeight = size.y
        }
        
        return mScreenHeight
    }
    
    @Px
    fun getScreenWidth(c: Context): Int {
        if (mScreenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            mScreenWidth = size.x
        }
        
        return mScreenWidth
    }
    
    @Px
    private var mScreenWidth = 0
    @Px
    private var mScreenHeight = 0
    
    @get:Px
    inline val screenHeight: Int
        get() = getScreenHeight(MeijueUi.get().context)
    
    @get:Px
    inline val screenWidth: Int
        get() = getScreenWidth(MeijueUi.get().context)
    
    
    //
    
    
    @Dimension(unit = Dimension.DP)
    private var mActionBarSizeInDp: Int = 0
    @Dimension(unit = Dimension.PX)
    private var mActionBarSize: Int = 0
    
    @get:Dimension(unit = Dimension.DP)
    val actionBarSizeInDp: Int
        get() {
            if (mActionBarSizeInDp == 0) {
                val styledAttributes = MeijueUi.get().context.theme.obtainStyledAttributes(
                    intArrayOf(android.R.attr.actionBarSize))
                mActionBarSizeInDp = styledAttributes.getDimension(0, 0f).toInt()
                styledAttributes.recycle()
            }
            return mActionBarSizeInDp
        }
    
    @get:Dimension(unit = Dimension.PX)
    val actionBarSize: Int
        get() {
            if (mActionBarSize == 0) {
                val styledAttributes = MeijueUi.get().context.theme.obtainStyledAttributes(
                    intArrayOf(android.R.attr.actionBarSize))
                mActionBarSize = styledAttributes.getDimensionPixelSize(0, 0)
                styledAttributes.recycle()
            }
            return mActionBarSize
        }
    
    @Dimension(unit = Dimension.DP)
    private var mStatusBarSizeInDp: Int = 0
    @Dimension(unit = Dimension.PX)
    private var mStatusBarSize: Int = 0
    
    @get:Dimension(unit = Dimension.DP)
    val statusBarHeightInDp: Int
        get() {
            if (mStatusBarSizeInDp == 0) {
                val res = MeijueUi.get().context.resources
                val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    mStatusBarSizeInDp = res.getDimension(resourceId).toInt()
                }
            }
            return mStatusBarSizeInDp
        }
    
    @get:Dimension(unit = Dimension.PX)
    val statusBarHeight: Int
        get() {
            if (mStatusBarSize == 0) {
                val res = MeijueUi.get().context.resources
                val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    mStatusBarSize = res.getDimensionPixelSize(resourceId)
                }
            }
            return mStatusBarSize
        }
    
    
    @Dimension(unit = Dimension.DP)
    private var mNavigationBarSizeInDp: Int = 0
    @Dimension(unit = Dimension.PX)
    private var mNavigationBarSize: Int = 0
    
    @get:Dimension(unit = Dimension.DP)
    val navigationBarHeightInDp: Int
        get() {
            if (mNavigationBarSizeInDp == 0) {
                val res = MeijueUi.get().context.resources
                val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0 && !hasMenuKey) {
                    mNavigationBarSizeInDp = res.getDimension(resourceId).toInt()
                }
            }
            return mNavigationBarSizeInDp
        }
    
    @get:Dimension(unit = Dimension.PX)
    val navigationBarHeight: Int
        get() {
            if (mNavigationBarSize == 0) {
                val res = MeijueUi.get().context.resources
                val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0 && !hasMenuKey) {
                    mNavigationBarSize = res.getDimensionPixelSize(resourceId)
                }
            }
            return mNavigationBarSize
        }
    
    val hasMenuKey: Boolean = ViewConfiguration.get(MeijueUi.get().context).hasPermanentMenuKey()
    
    
    //
    
    
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
    
            MeijueUi.get().context.let {
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
        MeijueUi.get().context.let {
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


fun Int.toPx(): Int = Utils.dp2px(this)
fun Int.toDp(): Int = Utils.px2dp(this)
fun Float.toPx(): Float = Utils.dp2px(this)
fun Float.toDp(): Float = Utils.px2dp(this)



