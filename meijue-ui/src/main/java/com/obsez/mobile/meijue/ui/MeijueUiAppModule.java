package com.obsez.mobile.meijue.ui;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * to initial Android `Context` into MeiJueUi Library.
 */
public class MeijueUiAppModule {

    private final static MeijueUiAppModule instance = new MeijueUiAppModule();

    public static MeijueUiAppModule get() {
        return instance;
    }

    private WeakReference<Context> context;

    //private final BehaviorSubject<Boolean> isConnectedSubject = BehaviorSubject.create();

    public void init(Context context) {
        get().context = new WeakReference<>(context);
    }

    public Context getContext() {
        return context == null ? null : context.get();
    }

}
