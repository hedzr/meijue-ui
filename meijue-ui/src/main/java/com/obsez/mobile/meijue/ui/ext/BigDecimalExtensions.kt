@file:JvmName("BigDecimalUtil")

package com.obsez.mobile.meijue.ui.ext

import com.obsez.mobile.meijue.ui.util.CurrencyUtil
import com.obsez.mobile.meijue.ui.util.EthUtil
import java.math.BigDecimal
import java.text.ParseException
import java.util.*

fun isValidDecimal(inputValue: String): Boolean {
    return try {
        parseValue(inputValue)
        true
    } catch (e: ParseException) {
        false
    }
}

fun createSafeBigDecimal(inputValue: String): BigDecimal {
    return try {
        parseValue(inputValue)
    } catch (e: ParseException) {
        return BigDecimal("0")
    }
}

@Throws(ParseException::class)
private fun parseValue(inputValue: String): BigDecimal {
    // BigDecimal doesn't handle ",", so force the locale to ENGLISH and replace , with .
    val df = CurrencyUtil.getNumberFormatWithOutGrouping(Locale.ENGLISH)
    df.maximumFractionDigits = EthUtil.BIG_DECIMAL_SCALE
    df.isParseBigDecimal = true
    val safeInput = inputValue.replace(",", ".")
    return df.parse(safeInput) as BigDecimal
}