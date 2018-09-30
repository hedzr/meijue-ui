package com.obsez.mobile.meijue.ui.ext

import android.os.Looper
import android.support.annotation.CheckResult
import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

// Extensions for RxJava2

/**
 * Safely dispose = if not null and not already disposed
 */
fun Disposable?.safeDispose() {
    if (this?.isDisposed == false) {
        dispose()
    }
}

/**
 * Overloaded operator to allow adding [Disposable] to [CompositeDisposable] via + sign
 */
operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable {
    this.add(disposable)
    return this
}


/////////////////////////////////////////////////////////////////

// Simple Rx2Binding


object Preconditions {
    fun checkArgument(assertion: Boolean, message: String) {
        if (!assertion) {
            throw IllegalArgumentException(message)
        }
    }
    
    fun <T> checkNotNull(value: T?, message: String): T {
        if (value == null) {
            throw NullPointerException(message)
        }
        return value
    }
    
    fun checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw IllegalStateException(
                "Must be called from the main thread. Was: " + Thread.currentThread())
        }
    }
}


/**
 * 防止按钮被重复点击
 */
class Rx2View(private val target: View, private val windowDuration: Long = 500, private val unit: TimeUnit = TimeUnit.MILLISECONDS) {
    /**
     * 防止重复点击
     *
     * @param target Target View
     * @param action The user-defined listener
     */
    fun setOnClickListener(action: (source: View) -> Unit) {
        onClick(target).throttleFirst(windowDuration, unit).subscribe { v -> v.isEnabled = false; action(v) }
    }
    
    /**
     * 监听onclick事件防抖动
     *
     * @param view
     * @return
     */
    @CheckResult
    private fun onClick(view: View): Observable<View> {
        Preconditions.checkNotNull(view, "view == null")
        return Observable.create(ViewClickOnSubscribe(view))
    }
    
    //    @CheckResult
    //    @NonNull
    //    public static <T extends Adapter> Observable<AdapterViewItemClickEvent> itemClickEvents(
    //            @NonNull RecyclerAdapter<?> view) {
    //        checkNotNull(view, "view == null");
    //        return Observable.create(new AdapterViewItemClickEventOnSubscribe(view));
    //    }
    
    /**
     * onClick event debouncer
     *
     */
    private class ViewClickOnSubscribe(private val view: View) : ObservableOnSubscribe<View> {
        
        @Throws(Exception::class)
        override fun subscribe(@io.reactivex.annotations.NonNull e: ObservableEmitter<View>) {
            Preconditions.checkUiThread()
            
            val listener = View.OnClickListener {
                if (!e.isDisposed) {
                    e.onNext(view)
                }
            }
            view.setOnClickListener(listener)
        }
    }
    
}

