package com.obsez.mobile.meijue.ui.base

import android.app.Application
import com.obsez.mobile.meijue.ui.MeijueUiAppModule

/**
 * Just a sample
 */
abstract class BaseApp : Application() {
    
    init {
        //
    }
    
    override fun onCreate() {
        super.onCreate()
        // 至少，Preference<T> 需要这里已完成正确的初始化
        MeijueUiAppModule.get().init(this)
    }
    
    //    override fun onTerminate() {
    //        super.onTerminate()
    //    }
}
