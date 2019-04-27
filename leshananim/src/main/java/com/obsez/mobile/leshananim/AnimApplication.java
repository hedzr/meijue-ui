package com.obsez.mobile.leshananim;

import android.app.Application;

import timber.log.Timber;

public class AnimApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        // no need: MeijueUi.get().init(this);
    }
}
