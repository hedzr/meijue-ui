package com.obsez.mobile.meijue.ui.ext

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.obsez.mobile.meijue.ui.R
import com.obsez.mobile.meijue.ui.view.HorizontalLineDivider

val <VH : androidx.recyclerview.widget.RecyclerView.ViewHolder>androidx.recyclerview.widget.RecyclerView.Adapter<VH>.isEmpty: Boolean
    get() {
        return (this.itemCount == 0)
    }


fun androidx.recyclerview.widget.RecyclerView.addHorizontalLineDivider(
    leftPadding: Int = getPxSize(R.dimen.avatar_size_small)
        + getPxSize(R.dimen.margin_primary)
        + getPxSize(R.dimen.margin_three_quarters),
    rightPadding: Int = getPxSize(R.dimen.margin_primary),
    color: Int = getColorById(R.color.divider),
    startPosition: Int = 0,
    skipNEndPositions: Int = 0
): HorizontalLineDivider {
    val divider = HorizontalLineDivider(color)
        .setRightPadding(rightPadding)
        .setLeftPadding(leftPadding)
        .setStartPosition(startPosition)
        .skipNEndPositions(skipNEndPositions)
    addItemDecoration(divider)
    return divider
}

fun androidx.recyclerview.widget.RecyclerView.addDivider(@DrawableRes id: Int, orientation: Int = androidx.recyclerview.widget.DividerItemDecoration.VERTICAL) {
    val divider = androidx.recyclerview.widget.DividerItemDecoration(context, orientation)
    divider.setDrawable(ContextCompat.getDrawable(context, id)!!)
    addItemDecoration(divider)
}


fun androidx.recyclerview.widget.RecyclerView.findOneVisibleChild(fromIndex: Int, toIndex: Int, completelyVisible: Boolean, acceptPartiallyVisible: Boolean): View? {
    if (layoutManager == null)
        return null
    
    val helper: androidx.recyclerview.widget.OrientationHelper = if (layoutManager!!.canScrollVertically()) {
        androidx.recyclerview.widget.OrientationHelper.createVerticalHelper(layoutManager)
    } else {
        androidx.recyclerview.widget.OrientationHelper.createHorizontalHelper(layoutManager)
    }
    
    val start = helper.startAfterPadding
    val end = helper.endAfterPadding
    val next = if (toIndex > fromIndex) 1 else -1
    var partiallyVisible: View? = null
    var i = fromIndex
    while (i != toIndex) {
        val child = layoutManager!!.getChildAt(i)
        val childStart = helper.getDecoratedStart(child)
        val childEnd = helper.getDecoratedEnd(child)
        if (childStart < end && childEnd > start) {
            if (completelyVisible) {
                if (childStart >= start && childEnd <= end) {
                    return child
                } else if (acceptPartiallyVisible && partiallyVisible == null) {
                    partiallyVisible = child
                }
            } else {
                return child
            }
        }
        i += next
    }
    return partiallyVisible
}

fun androidx.recyclerview.widget.RecyclerView.isVisibleItem(position: Int, completelyVisible: Boolean = false, acceptPartiallyVisible: Boolean = false): Boolean {
    val v = findOneVisibleChild(position, position, completelyVisible, acceptPartiallyVisible)
    return v != null
}

val androidx.recyclerview.widget.RecyclerView.firstVisibleItemPosition: Int
    inline get () {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(0, lm.childCount, false, true)
            return if (child == null) androidx.recyclerview.widget.RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return androidx.recyclerview.widget.RecyclerView.NO_POSITION
    }

val androidx.recyclerview.widget.RecyclerView.firstCompletelyVisibleItemPosition: Int
    get() {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(0, lm.childCount, true, false)
            return if (child == null) androidx.recyclerview.widget.RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return androidx.recyclerview.widget.RecyclerView.NO_POSITION
    }

val androidx.recyclerview.widget.RecyclerView.lastVisibleItemPosition: Int
    get() {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(lm.childCount - 1, -1, false, true)
            return if (child == null) androidx.recyclerview.widget.RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return androidx.recyclerview.widget.RecyclerView.NO_POSITION
    }

val androidx.recyclerview.widget.RecyclerView.lastCompletelyVisibleItemPosition: Int
    get() {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(lm.childCount - 1, -1, true, false)
            return if (child == null) androidx.recyclerview.widget.RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return androidx.recyclerview.widget.RecyclerView.NO_POSITION
    }





