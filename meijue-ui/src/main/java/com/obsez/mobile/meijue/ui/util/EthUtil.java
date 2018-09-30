package com.obsez.mobile.meijue.ui.util;

import com.obsez.mobile.meijue.ui.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Locale;

public class EthUtil {

    public static final int BIG_DECIMAL_SCALE = 16;
    private static final int NUM_ETH_DECIMAL_PLACES = 6;
    public static final int NUM_FIAT_DECIMAL_PLACES = 2;
    private static final String USER_VISIBLE_STRING_FORMATTING = "%.6f";
    private static final BigDecimal weiToEthRatio = new BigDecimal("1000000000000000000");
    public static final String FIAT_FORMAT = "0.00";
    public static final String ETH_FORMAT = "0.000000";

    public static String hexAmountToUserVisibleString(final String hexEncodedWei) {
        final BigInteger wei = TypeConverter.StringHexToBigInteger(hexEncodedWei);
        return weiAmountToUserVisibleString(wei);
    }

    public static String weiAmountToUserVisibleString(final BigInteger wei) {
        final BigDecimal eth = weiToEth(wei);
        return ethAmountToUserVisibleString(eth);
    }

    public static String ethAmountToUserVisibleString(final BigDecimal eth) {
        return String.format(
            LocaleUtil.getLocale(),
            USER_VISIBLE_STRING_FORMATTING,
            eth.setScale(NUM_ETH_DECIMAL_PLACES, BigDecimal.ROUND_DOWN));
    }

    public static BigDecimal weiToEth(final BigInteger wei) {
        if (wei == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(wei)
            .divide(weiToEthRatio)
            .setScale(BIG_DECIMAL_SCALE, BigDecimal.ROUND_DOWN);
    }

    public static BigInteger ethToWei(final BigDecimal amountInEth) {
        return amountInEth.multiply(weiToEthRatio).toBigInteger();
    }

    public static BigInteger ethToWei(final String amountInEth, final int decimals) {
        return new BigDecimal(amountInEth)
            .multiply(new BigDecimal("10")
                .pow(decimals)).toBigInteger();
    }

    public static String encodeToHex(final String value) throws NumberFormatException, NullPointerException {
        return String.format("%s%s", "0x", new BigInteger(value).toString(16));
    }

    public static boolean isLargeEnoughForSending(final BigDecimal eth) {
        return eth.setScale(NUM_ETH_DECIMAL_PLACES, BigDecimal.ROUND_DOWN).compareTo(BigDecimal.ZERO) == 1;
    }

    public static String decimalStringToEncodedEthAmount(final String decimalString) {
        if (decimalString.isEmpty()) return "0x0";
        final BigInteger weiAmount = EthUtil.ethToWei(new BigDecimal(decimalString));
        return TypeConverter.toJsonHex(weiAmount);
    }

    public static String ethToFiat(final ExchangeRate exchangeRate, final BigDecimal ethAmount) {
        final BigDecimal marketRate = exchangeRate.getRate();
        final BigDecimal fiatAmount = marketRate.multiply(ethAmount, MathContext.DECIMAL64);
        final DecimalFormat df = CurrencyUtil.getNumberFormatWithOutGrouping(Locale.ENGLISH);
        df.applyPattern(FIAT_FORMAT);
        return df.format(fiatAmount);
    }

    public static String fiatToEth(final ExchangeRate exchangeRate, final BigDecimal fiatAmount) {
        final BigDecimal marketRate = exchangeRate.getRate();
        final BigDecimal ethAmount = fiatAmount.divide(marketRate, BIG_DECIMAL_SCALE, BigDecimal.ROUND_DOWN);
        final DecimalFormat df = CurrencyUtil.getNumberFormatWithOutGrouping(Locale.ENGLISH);
        df.applyPattern(ETH_FORMAT);
        return df.format(ethAmount);
    }
}
