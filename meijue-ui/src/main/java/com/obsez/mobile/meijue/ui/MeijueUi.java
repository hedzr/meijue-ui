package com.obsez.mobile.meijue.ui;

import android.content.Context;

/**
 * to initial Android `Context` into MeiJueUi Library.
 */
public class MeijueUi {

    private final static MeijueUi instance = new MeijueUi();

    public static MeijueUi get() {
        return instance;
    }

    public Context getContext() {
        assert MeijueUiProvider.context != null;
        return MeijueUiProvider.context;
    }

    //private WeakReference<Context> context;
    //
    ////private final BehaviorSubject<Boolean> isConnectedSubject = BehaviorSubject.create();
    //
    //public void init(Context context) {
    //    get().context = new WeakReference<>(context);
    //}
    //
    //public Context getContext() {
    //    return context == null ? null : context.get();
    //}

}
