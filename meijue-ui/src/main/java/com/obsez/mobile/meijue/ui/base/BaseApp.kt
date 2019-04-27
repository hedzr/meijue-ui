package com.obsez.mobile.meijue.ui.base

import android.app.Application

/**
 * Just a sample Application class
 */
abstract class BaseApp : Application() {
    
    init {
        //
    }
    
    override fun onCreate() {
        super.onCreate()
    
        //        // 至少，Preference<T> 需要这里已完成正确的初始化
        //        MeijueUi.get().init(this)
    }
    
    //    override fun onTerminate() {
    //        super.onTerminate()
    //    }
}
