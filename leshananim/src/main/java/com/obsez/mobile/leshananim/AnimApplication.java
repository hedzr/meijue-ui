package com.obsez.mobile.leshananim;

import android.app.Application;

import com.obsez.mobile.meijue.ui.MeijueUiAppModule;

import timber.log.Timber;

public class AnimApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        MeijueUiAppModule.get().init(this);
    }
}
