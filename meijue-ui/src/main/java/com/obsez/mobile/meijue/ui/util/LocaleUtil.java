package com.obsez.mobile.meijue.ui.util;

import android.annotation.TargetApi;
import android.os.Build;

import com.obsez.mobile.meijue.ui.MeijueUi;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LocaleUtil {

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getLocale() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return MeijueUi.get().getContext().getResources().getConfiguration().getLocales().get(
                    0);
            } else {
                return MeijueUi.get().getContext().getResources().getConfiguration().locale;
            }
        } catch (final NullPointerException ex) {
            // TODO LogUtil.exception("NPE when getting locale", ex);
            // Default to something!
            return Locale.ENGLISH;
        }
    }

    public static DecimalFormatSymbols getDecimalFormatSymbols() {
        return new DecimalFormatSymbols(getLocale());
    }
}
