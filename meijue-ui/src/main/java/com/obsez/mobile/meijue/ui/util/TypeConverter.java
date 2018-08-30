package com.obsez.mobile.meijue.ui.util;

import android.support.annotation.Nullable;

import com.obsez.mobile.meijue.ui.ext.BigDecimalUtil;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

//import org.spongycastle.util.encoders.Hex;

public class TypeConverter {

    public static BigInteger StringHexToBigInteger(final String input) {
        if (input == null) {
            return BigInteger.ZERO;
        }

        final String hexa = input.startsWith("0x") ? input.substring(2) : input;
        try {
            return new BigInteger(hexa, 16);
        } catch (final NumberFormatException ex) {
            return BigInteger.ZERO;
        }
    }

    public static byte[] StringHexToByteArray(String x) {
        if (x.startsWith("0x")) {
            x = x.substring(2);
        }
        if (x.length() % 2 != 0) x = "0" + x;
        //return Hex.decode(x);
        return fromHexString(x);
    }

    public static String toHexString(byte[] ba) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < ba.length; i++) {
            hex.append(zeroPad(Integer.toHexString(ba[i] & 0xFF).toUpperCase(), 2));
        }
        return hex.toString();
    }

    public static String zeroPad(String s, int n) {
        if (s.length() >= n) return s;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        sb.append(s);
        s = sb.toString();
        return s.substring(s.length() - n);
    }

    public static byte[] fromHexString(String hex) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            bas.write(b);
        }
        return bas.toByteArray();
    }

    public static String jsonStringToString(final String jsonString) {
        if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
            return jsonString.substring(1,
                    jsonString.length() - 1);
        }
        if (jsonString.startsWith("\"")) return jsonString.substring(1);
        if (jsonString.endsWith("\"")) return jsonString.substring(0, jsonString.length() - 1);
        return jsonString;
    }

    public static String toJsonHex(final byte[] x) {
        return "0x" + toHexString(x);
    }

    public static String toJsonHex(final String x) {
        if (x.startsWith("0x")) return x;
        return "0x" + x;
    }

    public static String toJsonHex(final long n) {
        return "0x" + Long.toHexString(n);
    }

    public static String toJsonHex(final BigInteger n) {
        return "0x" + n.toString(16);
    }

    public static String fromHexToDecimal(final String input) {
        final String hex = input.startsWith("0x") ? input.substring(2) : input;
        return String.valueOf(new BigInteger(hex, 16));
    }

    //public static String skeletonAndSignatureToRLPEncodedHex(final String skeleton, final String signature) {
    //    final Object[] decoded = (Object[])RLP.decode(TypeConverter.StringHexToByteArray(skeleton), 0)
    // .getDecoded();
    //
    //    if (decoded.length != 9) {
    //        throw new IllegalStateException("Invalid Transaction Skeleton: Decoded RLP length is wrong");
    //    }
    //
    //    if (!isEmptyString(decoded[decoded.length - 2])
    //        ||!isEmptyString(decoded[decoded.length - 1])) {
    //        throw new IllegalStateException("Transaction is already signed!");
    //    }
    //
    //    final BigInteger r = TypeConverter.StringHexToBigInteger(signature.substring(2, 66));
    //    final BigInteger s = TypeConverter.StringHexToBigInteger(signature.substring(66, 130));
    //    final int v = TypeConverter.StringHexToBigInteger(signature.substring(130)).intValue();
    //    final int vee = getVee(decoded[decoded.length - 3]);
    //
    //    decoded[decoded.length - 3] = v + vee;
    //    decoded[decoded.length - 2] = r;
    //    decoded[decoded.length - 1] = s;
    //    return TypeConverter.toJsonHex(RLP.encode(decoded));
    //}
    //
    //private static int getVee(final Object obj) {
    //    if (obj instanceof byte[]) {
    //        int networkId = RLP.decodeInt((byte[]) obj, 0);
    //        return 35 + networkId * 2;
    //    }
    //    return 27;
    //}

    private static boolean isEmptyString(final Object obj) {
        return obj instanceof String && ((String) obj).length() == 0;
    }

    // Set pattern to null if you want the original format
    public static String formatHexString(final String value, final int decimals,
            @Nullable final String pattern) {
        final String decimalValue = StringHexToBigInteger(value).toString();
        final DecimalFormat df = getDecimalFormat(pattern);
        if (decimals > 0) {
            final BigDecimal paddedDecimalValue = getPaddedDecimalValue(decimalValue, decimals);
            return df != null ? df.format(paddedDecimalValue) : paddedDecimalValue.toString();
        } else {
            return df != null ? df.format(new BigDecimal(decimalValue)) : decimalValue;
        }
    }

    private static BigDecimal getPaddedDecimalValue(final String decimalValue, final int decimals) {
        final char separator = LocaleUtil.getDecimalFormatSymbols().getMonetaryDecimalSeparator();
        final String paddingFormat = "%0" + decimals + "d";
        final String paddedDecimalValue = String.format(LocaleUtil.getLocale(), paddingFormat,
                new BigInteger(decimalValue));
        final int decimalPosition = paddedDecimalValue.length() - decimals;
        return BigDecimalUtil.createSafeBigDecimal(
                new StringBuilder(paddedDecimalValue)
                        .insert(decimalPosition, separator)
                        .toString()
        ).stripTrailingZeros();
    }

    private static DecimalFormat getDecimalFormat(@Nullable final String pattern) {
        if (pattern != null) {
            final DecimalFormat df = CurrencyUtil.getNumberFormatWithOutGrouping();
            df.applyPattern(pattern);
            df.setRoundingMode(RoundingMode.DOWN);
            return df;
        } else {
            return null;
        }
    }

    public static String formatNumber(final int value, final String format) {
        final DecimalFormat df = CurrencyUtil.getNumberFormatWithOutGrouping();
        df.applyPattern(format);
        return df.format(value);
    }
}
