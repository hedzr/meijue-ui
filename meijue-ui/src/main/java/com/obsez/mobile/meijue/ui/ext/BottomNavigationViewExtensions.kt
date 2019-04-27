package com.obsez.mobile.meijue.ui.ext

//import android.support.design.internal.BottomNavigationItemView
//import android.support.design.internal.BottomNavigationMenuView
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.obsez.mobile.meijue.ui.util.Utils.dp2px


fun BottomNavigationView.setField(targetClass: Class<*>, instance: Any, fieldName: String, value: Any) {
    try {
        val field = targetClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(instance, value)
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
    
}

fun <T> BottomNavigationView.getField(targetClass: Class<*>, instance: Any, fieldName: String): T? {
    try {
        val field = targetClass.getDeclaredField(fieldName)
        field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return field.get(instance) as T
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
    
    return null
}

//fun BottomNavigationView.dp2px(dp: Int): Int {
//    return (dp * Resources.getSystem().displayMetrics.density).toInt()
//}
//
//fun BottomNavigationView.dp2px(dp: Float): Float {
//    return (dp * Resources.getSystem().displayMetrics.density)
//}

fun BottomNavigationView.getBottomNavigationMenuView() = this.getChildAt(0) as BottomNavigationMenuView

fun BottomNavigationView.getBottomNavigationItemViews(): Array<BottomNavigationItemView>? {
    val mMenuView = getBottomNavigationMenuView()
    return getField(mMenuView.javaClass, mMenuView, "buttons")
}

fun BottomNavigationView.getBottomNavigationItemView(position: Int) = getBottomNavigationItemViews()?.get(position)

/**
 * androidx: ?? 使用 {@link BottomNavigationView#setItemHorizontalTranslationEnabled(true)} 代替
 */
@SuppressLint("RestrictedApi")
fun BottomNavigationView.enableShiftingMode(enable: Boolean) {
    /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;
        2. change field mShiftingMode value in mMenuView
        private boolean mShiftingMode = true;
         */
    // 1. get mMenuView
    val mMenuView = getBottomNavigationMenuView()
    // 2. change field mShiftingMode value in mMenuView
    setField(mMenuView.javaClass, mMenuView, "isShifting", enable)
    
    mMenuView.updateMenuView()
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.enableShiftingMode(position: Int, enable: Boolean) {
    getBottomNavigationItemView(position)?.setShifting(enable)
}

/**
 * androidx: ?? 使用 {@link BottomNavigationView#setItemHorizontalTranslationEnabled(true)} 代替
 */
@SuppressLint("RestrictedApi")
fun BottomNavigationView.enableItemShiftingMode(enable: Boolean) {
    /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;
        2. get field in this mMenuView
        private BottomNavigationItemView[] mButtons;
        3. change field mShiftingMode value in mButtons
        private boolean mShiftingMode = true;
         */
    // 1. get mMenuView
    val mMenuView = getBottomNavigationMenuView()
    // 2. get mButtons
    val mButtons = getBottomNavigationItemViews()
    // 3. change field mShiftingMode value in mButtons
    if (mButtons != null) {
        for (button in mButtons) {
            setField(button.javaClass, button, "isShifting", enable)
        }
    }
    mMenuView.updateMenuView()
}

/**
 * androidx: ?? 使用 {@link BottomNavigationView#setItemHorizontalTranslationEnabled(false)} 代替
 */
@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        
        for (i in 0 until menuView.childCount) {
            val itemView = menuView.getChildAt(i) as BottomNavigationItemView
            itemView.setShifting(false)
            itemView.setChecked(itemView.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        // Log
    } catch (e: IllegalAccessException) {
        // Log
    }
}

fun BottomNavigationView.getCurrentItem(): Int {
    /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;
        2. get field in mMenuView
        private BottomNavigationItemView[] mButtons;
        3. get menu and traverse it to get the checked one
         */
    
    // 1. get mMenuView
    //        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
    // 2. get mButtons
    val mButtons = getBottomNavigationItemViews()
    // 3. get menu and traverse it to get the checked one
    if (mButtons != null) {
        for (i in mButtons.indices) {
            if (menu.getItem(i).isChecked) {
                return i
            }
        }
    }
    return 0
}

fun BottomNavigationView.getMenuItemPosition(item: MenuItem): Int {
    // get item id
    val itemId = item.itemId
    val size = menu.size()
    for (i in 0 until size) {
        if (menu.getItem(i).itemId == itemId) {
            return i
        }
    }
    return -1
}

fun BottomNavigationView.setCurrentItem(item: Int) {
    // check bounds
    if (item < 0 || item >= maxItemCount) {
        throw ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - ${maxItemCount - 1}. Actually $item")
    }
    
    /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;
        2. get field in mMenuView
        private BottomNavigationItemView[] mButtons;
        private final OnClickListener mOnClickListener;
        3. call mOnClickListener.onClick();
     */
    // 1. get mMenuView
    val mMenuView = getBottomNavigationMenuView()
    // 2. get mButtons
    val mButtons = getBottomNavigationItemViews()
    // get mOnClickListener
    val mOnClickListener: View.OnClickListener? = this.getField(mMenuView.javaClass, mMenuView, "onClickListener")
    
    //        System.out.println("mMenuView:" + mMenuView + " mButtons:" + mButtons + " mOnClickListener" + mOnClickListener);
    // 3. call mOnClickListener.onClick();
    mOnClickListener?.onClick(mButtons?.get(item))
}

fun BottomNavigationView.getIconAt(position: Int): ImageView? {
    /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         * 3 private ImageView mIcon;
         */
    val mButtons = getBottomNavigationItemView(position)
    return getField(BottomNavigationItemView::class.java, mButtons!!, "icon")
}

fun BottomNavigationView.getSmallLabelAt(position: Int): TextView? {
    /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         * 3 private final TextView mSmallLabel;
         */
    val mButtons = getBottomNavigationItemView(position)
    return getField(BottomNavigationItemView::class.java, mButtons!!, "smallLabel")
}

fun BottomNavigationView.getLargeLabelAt(position: Int): TextView? {
    /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         * 3 private final TextView mLargeLabel;
         */
    val mButtons = getBottomNavigationItemView(position)
    return getField(BottomNavigationItemView::class.java, mButtons!!, "largeLabel")
}

fun BottomNavigationView.getItemCount(): Int {
    val bottomNavigationItemViews = getBottomNavigationItemViews() ?: return 0
    return bottomNavigationItemViews.size
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setSmallTextSize(sp: Float) {
    val count = getItemCount()
    for (i in 0 until count) {
        getSmallLabelAt(i)?.textSize = sp
    }
    val mMenuView = getBottomNavigationMenuView()
    mMenuView.updateMenuView()
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setLargeTextSize(sp: Float) {
    val count = getItemCount()
    for (i in 0 until count) {
        getLargeLabelAt(i)?.textSize = sp
    }
    val mMenuView = getBottomNavigationMenuView()
    mMenuView.updateMenuView()
}

fun BottomNavigationView.setTextSize(sp: Float) {
    setLargeTextSize(sp)
    setSmallTextSize(sp)
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setIconSizeAt(position: Int, width: Float, height: Float) {
    val icon = getIconAt(position)
    // update size
    val layoutParams = icon?.layoutParams
    layoutParams?.width = dp2px(width).toInt()
    layoutParams?.height = dp2px(height).toInt()
    icon?.layoutParams = layoutParams
    val mMenuView = getBottomNavigationMenuView()
    mMenuView.updateMenuView()
}

fun BottomNavigationView.setIconSize(width: Float, height: Float) {
    val count = getItemCount()
    for (i in 0 until count) {
        setIconSizeAt(i, width, height)
    }
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setItemHeight(height: Int) {
    // 1. get mMenuView
    val mMenuView = getBottomNavigationMenuView()
    // 2. set private final int mItemHeight in mMenuView
    setField(mMenuView.javaClass, mMenuView, "itemHeight", height)
    
    mMenuView.updateMenuView()
}

fun BottomNavigationView.getItemHeight(): Int {
    // 1. get mMenuView
    val mMenuView = getBottomNavigationMenuView()
    // 2. get private final int mItemHeight in mMenuView
    return getField<Int>(mMenuView.javaClass, mMenuView, "itemHeight")!!
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setTypeface(typeface: Typeface, style: Int) {
    val count = getItemCount()
    for (i in 0 until count) {
        getLargeLabelAt(i)?.setTypeface(typeface, style)
        getSmallLabelAt(i)?.setTypeface(typeface, style)
    }
    val mMenuView = getBottomNavigationMenuView()
    mMenuView.updateMenuView()
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setTypeface(typeface: Typeface) {
    val count = getItemCount()
    for (i in 0 until count) {
        getLargeLabelAt(i)?.typeface = typeface
        getSmallLabelAt(i)?.typeface = typeface
    }
    val mMenuView = getBottomNavigationMenuView()
    mMenuView.updateMenuView()
}


@SuppressLint("RestrictedApi")
fun BottomNavigationView.setItemBackground(position: Int, background: Int) {
    getBottomNavigationItemView(position)?.setItemBackground(background)
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setIconTintList(position: Int, tint: ColorStateList) {
    getBottomNavigationItemView(position)?.setIconTintList(tint)
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setTextTintList(position: Int, tint: ColorStateList) {
    getBottomNavigationItemView(position)?.setTextColor(tint)
}

fun BottomNavigationView.setIconsMarginTop(marginTop: Int) {
    for (i in 0 until getItemCount()) {
        setIconMarginTop(i, marginTop)
    }
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.setIconMarginTop(position: Int, marginTop: Int) {
    /*
        1. BottomNavigationItemView
        2. private final int mDefaultMargin;
         */
    val itemView = getBottomNavigationItemView(position)
    if (itemView != null) {
        setField(BottomNavigationItemView::class.java, itemView, "defaultMargin", marginTop)
    }
    val mMenuView = getBottomNavigationMenuView()
    mMenuView.updateMenuView()
}

