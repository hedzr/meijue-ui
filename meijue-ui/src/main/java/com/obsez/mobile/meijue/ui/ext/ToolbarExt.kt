package com.obsez.mobile.meijue.ui.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar


/**
 * Helper class that iterates through Toolbar views, and sets dynamically icons and texts color
 * Created by chomi3 on 2015-01-19.
 */
object ToolbarColorizeHelper {
    
    /**
     * Use this method to colorize toolbar icons to the desired target color
     * @param toolbarView toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     * @param activity reference to activity needed to register observers
     */
    fun colorizeToolbar(toolbarView: Toolbar, toolbarIconsColor: Int, activity: Activity) {
        val colorFilter = PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY)
        
        for (i in 0 until toolbarView.childCount) {
            val v = toolbarView.getChildAt(i)
            
            //Step 1 : Changing the color of back button (or open drawer button).
            if (v is ImageButton) {
                //Action Bar back button
                v.drawable.colorFilter = colorFilter
            }
            
            
            if (v is ActionMenuView) {
                for (j in 0 until v.childCount) {
                    
                    //Step 2: Changing the color of any ActionMenuViews - icons that are not back button, nor text, nor overflow menu icon.
                    //Colorize the ActionViews -> all icons that are NOT: back button | overflow menu
                    val innerView = v.getChildAt(j)
                    if (innerView is ActionMenuItemView) {
                        for (k in 0 until innerView.compoundDrawables.size) {
                            if (innerView.compoundDrawables[k] != null) {
                                
                                //Important to set the color filter in seperate thread, by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post { innerView.compoundDrawables[k].colorFilter = colorFilter }
                            }
                        }
                    }
                }
            }
            
            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor)
            toolbarView.setSubtitleTextColor(toolbarIconsColor)
            
            //Step 4: Changing the color of the Overflow Menu icon.
            setOverflowButtonColor(activity, colorFilter)
        }
    }
    
    /**
     * It's important to set overflowDescription atribute in styles, so we can grab the reference
     * to the overflow icon. Check: res/values/styles.xml
     * @param activity
     * @param colorFilter
     */
    private fun setOverflowButtonColor(activity: Activity, colorFilter: PorterDuffColorFilter) {
        val overflowDescription = "More options" // activity.getString(R.string.abc_action_menu_overflow_description)
        val decorView = activity.window.decorView as ViewGroup
        val viewTreeObserver = decorView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val outViews = ArrayList<View>()
                decorView.findViewsWithText(outViews, overflowDescription,
                    View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
                if (outViews.isEmpty()) {
                    return
                }
                val overflowP: ActionMenuView = outViews[0].parent as ActionMenuView
                overflowP.overflowIcon?.colorFilter = colorFilter // PorterDuff.Mode.SRC_ATOP
                removeOnGlobalLayoutListener(decorView, this)
            }
        })
    }
    
    @Suppress("DEPRECATION")
    @SuppressLint("ObsoleteSdkInt")
    private fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        } else {
            v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}


/**
 * https://snow.dog/blog/how-to-dynamicaly-change-android-toolbar-icons-color/
 */
fun Toolbar.colorize(toolbarIconsColor: Int) {
    ToolbarColorizeHelper.colorizeToolbar(this, toolbarIconsColor, this.context as Activity)
}


fun Toolbar.changeNavigateionIconColor(@ColorInt color: Int) {
    this.navigationIcon?.apply {
        mutate()
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

fun Menu.changeMenuIconColor(@ColorInt color: Int) {
    for (i in 0 until this.size()) {
        val drawable = this.getItem(i).icon ?: continue
        drawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

fun Toolbar.changeOverflowMenuIconColor(@ColorInt color: Int) {
    try {
        this.overflowIcon?.apply {
            mutate()
            setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    } catch (e: Exception) {
    }
    
}
