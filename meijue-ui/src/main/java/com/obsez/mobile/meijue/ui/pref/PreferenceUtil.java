package com.obsez.mobile.meijue.ui.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.obsez.mobile.meijue.ui.pref.impl.SharedPreferenceProxy;

import androidx.annotation.NonNull;

public class PreferenceUtil {

    // https://github.com/liyuanhust/MultiprocessPreference

    public static final String METHOD_CONTAIN_KEY = "method_contain_key";
    public static final String AUTHORITY = "com.obsez.mobile.ebk";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String METHOD_QUERY_VALUE = "method_query_value";
    public static final String METHOD_EIDIT_VALUE = "method_edit";
    public static final String METHOD_QUERY_PID = "method_query_pid";
    public static final String KEY_VALUES = "key_result";


    public static final Uri sContentCreate = Uri.withAppendedPath(URI, "create");

    public static final Uri sContentChanged = Uri.withAppendedPath(URI, "changed");

    /**
     * 暂不推荐
     */
    public static SharedPreferences getSharedPreference(@NonNull Context ctx, String prefName) {
        return SharedPreferenceProxy.getSharedPreferences(ctx, prefName);
    }

    public static SharedPreferences get(@NonNull Context ctx, String prefName) {
        return SharedPreferenceProxy.getSharedPreferences(ctx, ctx.getPackageName() + prefName);
    }

    /**
     * = PreferenceManager.getDefaultSharedPreferences(ctx)
     * 推荐使用
     *
     * @param ctx application context
     * @return Preferences object
     * @see android.preference.PreferenceManager#getDefaultSharedPreferences(Context)
     */
    public static SharedPreferences get(@NonNull Context ctx) {
        //return SharedPreferenceProxy.getSharedPreferences(ctx, ctx.getPackageName() + "_preferences");
        return ctx.getSharedPreferences(ctx.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

}
