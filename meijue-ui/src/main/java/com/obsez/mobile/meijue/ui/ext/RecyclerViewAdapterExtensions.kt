package com.obsez.mobile.meijue.ui.ext

import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.obsez.mobile.meijue.ui.R
import com.obsez.mobile.meijue.ui.view.HorizontalLineDivider

val <VH : RecyclerView.ViewHolder>RecyclerView.Adapter<VH>.isEmpty: Boolean
    get() {
        return (this.itemCount == 0)
    }


fun RecyclerView.addHorizontalLineDivider(
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

fun RecyclerView.addDivider(@DrawableRes id: Int, orientation: Int = DividerItemDecoration.VERTICAL) {
    val divider = DividerItemDecoration(context, orientation)
    divider.setDrawable(ContextCompat.getDrawable(context, id)!!)
    addItemDecoration(divider)
}


fun RecyclerView.findOneVisibleChild(fromIndex: Int, toIndex: Int, completelyVisible: Boolean, acceptPartiallyVisible: Boolean): View? {
    if (layoutManager == null)
        return null
    
    val helper: OrientationHelper = if (layoutManager!!.canScrollVertically()) {
        OrientationHelper.createVerticalHelper(layoutManager)
    } else {
        OrientationHelper.createHorizontalHelper(layoutManager)
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

fun RecyclerView.isVisibleItem(position: Int, completelyVisible: Boolean = false, acceptPartiallyVisible: Boolean = false): Boolean {
    val v = findOneVisibleChild(position, position, completelyVisible, acceptPartiallyVisible)
    return v != null
}

val RecyclerView.firstVisibleItemPosition: Int
    inline get () {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(0, lm.childCount, false, true)
            return if (child == null) RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return RecyclerView.NO_POSITION
    }

val RecyclerView.firstCompletelyVisibleItemPosition: Int
    get() {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(0, lm.childCount, true, false)
            return if (child == null) RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return RecyclerView.NO_POSITION
    }

val RecyclerView.lastVisibleItemPosition: Int
    get() {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(lm.childCount - 1, -1, false, true)
            return if (child == null) RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return RecyclerView.NO_POSITION
    }

val RecyclerView.lastCompletelyVisibleItemPosition: Int
    get() {
        layoutManager?.let { lm ->
            val child = findOneVisibleChild(lm.childCount - 1, -1, true, false)
            return if (child == null) RecyclerView.NO_POSITION else this.getChildAdapterPosition(child)
        }
        return RecyclerView.NO_POSITION
    }





