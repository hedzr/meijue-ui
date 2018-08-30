package com.obsez.mobile.meijue.ui.util;

import com.obsez.mobile.meijue.ui.exception.CurrencyException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtil {

    public static DecimalFormat getNumberFormat() {
        final DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(
                LocaleUtil.getLocale());
        final DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        return numberFormat;
    }

    public static DecimalFormat getNumberFormatWithOutGrouping() {
        final DecimalFormat numberFormat = getNumberFormat();
        numberFormat.setGroupingUsed(false);
        return numberFormat;
    }

    public static DecimalFormat getNumberFormatWithOutGrouping(final Locale locale) {
        final DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        final DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        numberFormat.setGroupingUsed(false);
        return numberFormat;
    }

    public static String getCurrencyFromLocale() throws CurrencyException {
        try {
            final Currency currency = Currency.getInstance(LocaleUtil.getLocale());
            return currency.getCurrencyCode();
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new CurrencyException(new Throwable("Unsupported currency"));
        }
    }

    public static String getCode(final String currency) {
        try {
            return Currency.getInstance(currency).getCurrencyCode();
        } catch (NullPointerException | IllegalArgumentException e) {
            // TODO LogUtil.exception("Error during getting code from currency", e);
            return currency;
        }
    }

    public static String getSymbol(final String currencyCode) {
        try {
            final Currency currency = Currency.getInstance(currencyCode);
            if (currency.getSymbol().length() > 1) return "";
            return currency.getSymbol();
        } catch (NullPointerException | IllegalArgumentException e) {
            // TODO LogUtil.exception("Error during getting symbol from currency", e);
            return "";
        }
    }
}
