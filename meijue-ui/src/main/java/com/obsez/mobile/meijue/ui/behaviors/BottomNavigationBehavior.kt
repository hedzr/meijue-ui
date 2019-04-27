package com.obsez.mobile.meijue.ui.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.obsez.mobile.meijue.ui.ext.dp2px
import kotlin.math.max
import kotlin.math.min


class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {
    
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
    
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }
}


class BottomNavigationBehaviorWithSnackbar<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {
    
    
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
    
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }
    
    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }
    
    private fun updateSnackbar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
        if (snackbarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
            val params = snackbarLayout.layoutParams as CoordinatorLayout.LayoutParams
            
            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackbarLayout.layoutParams = params
        }
    }
}


class BottomNavigationFabBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }
    
    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        child.translationY = child.context.dp2px(-56f) // 56f: ActionBarSize or default BottomNavigationView height.
    }
    
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return updateButton(child, dependency)
    }
    
    private fun updateButton(child: View, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            val oldTranslation = child.translationY
            val height = dependency.height.toFloat()
            val newTranslation = dependency.translationY - height
            child.translationY = newTranslation
            
            return oldTranslation != newTranslation
        }
        return false
    }
}


class ScrollAwareFABBehavior constructor(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context, attrs) {
    
    //private boolean enabled = true;
    
    //public void setEnabled(boolean enabled) {
    //    this.enabled = enabled;
    //}
    
    
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     nestedScrollAxes: Int): Boolean {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
            super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                nestedScrollAxes)
    }
    
    
    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, fab: FloatingActionButton,
                                    target: View) {
        if (!isVisible(fab)) {
            fab.clearAnimation()
            animateIn(fab)
        }
        //Logger.d("animateIn(fab)");
    }
    
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                fab: FloatingActionButton,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int) {
        super.onNestedScroll(coordinatorLayout, fab, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (!isAutoHideEnabled) return
        if (dyConsumed != 0) {
            if (isVisible(fab)) {
                hideImmediately(fab)
            }
        }
        if (dyUnconsumed < 3) {
            if (!isVisible(fab)) {
                fab.clearAnimation()
                animateIn(fab)
            }
        }
        //if (dyConsumed > 0 && isVisible(fab)) {
        //    // User scrolled down and the FAB is currently visible -> hide the FAB
        //    hide1(fab);
        //    //animateOut(fab);
        //    //Logger.d("animateOut(fab)");
        //} else if (dyConsumed < 0 && !isVisible(fab)) {
        //    // User scrolled up and the FAB is currently not visible -> show the FAB
        //    //fab.postDelayed(new Runnable() {
        //    //    @Override
        //    //    public void run() {
        //    //        fab.show();
        //    //    }
        //    //}, 200L);
        //    animateIn(fab);
        //    //Logger.d("animateIn(fab)");
        //}
    }
    
    
    private fun isVisible(fab: FloatingActionButton): Boolean {
        //if (fab.getVisibility() != View.VISIBLE) {
        //    return false;
        //}
        return !(fab.alpha < 1f || fab.scaleX < 1f || fab.scaleY < 1f)
        //Logger.d("fab is visible");
    }
    
    private fun animateIn(fab: FloatingActionButton) {
        //Logger.d("animateIn(fab). alpha=%f, w=%d, h=%d, sx=%f, sy=%f", fab.getAlpha(), fab.getWidth(), fab
        // .getHeight(), fab.getScaleX(), fab.getScaleY());
        //        ViewCompat.animate(fab)
        //            .scaleX(1f).scaleY(1f)
        //            .alpha(1f)
        //            .setDuration(200)
        //            .setStartDelay(500L)
        //            .start()
        fab.animate()
            .scaleX(1f).scaleY(1f)
            .alpha(1f)
            .setDuration(200)
            .setStartDelay(500L)
            .setListener(null)
            .start()
    }
    
    private fun animateOut(fab: FloatingActionButton) {
        hideFab(fab)
    }
    
    private fun hideImmediately(fab: FloatingActionButton) {
        fab.alpha = 0f
        fab.scaleX = 0f
        fab.scaleY = 0f
        fab.show()
    }
    
    private fun hideFabSilently(fab: FloatingActionButton?) {
        if (fab != null) fab.alpha = 0f
    }
    
    private fun hideFab(fab: FloatingActionButton) {
        //if (fab != null) {
        //        ViewCompat.animate(fab)
        //            .scaleX(0f).scaleY(0f)
        //            .alpha(0f)
        //            .setDuration(100)
        //            .start()
        //}
        fab.animate()
            .scaleX(0f).scaleY(0f)
            .alpha(0f)
            .setDuration(100)
            .setListener(null)
            .start()
    }
    
    private fun showFab(fab: FloatingActionButton) {
        //if (mFragment != null && mFab != null &&
        //        (mFragment instanceof FragmentHeadersSections ||
        //                mFragment instanceof FragmentDataBinding ||
        //                mFragment instanceof FragmentStaggeredLayout ||
        //                mFragment instanceof FragmentAsyncFilter)) {
        //        ViewCompat.animate(fab)
        //            .scaleX(1f).scaleY(1f)
        //            .alpha(1f).setDuration(200)
        //            .setStartDelay(300L)
        //            .start()
        //}
        fab.animate()
            .scaleX(1f).scaleY(1f)
            .alpha(1f).setDuration(200)
            .setStartDelay(300L)
            .setListener(null)
            .start()
    }
    
}


