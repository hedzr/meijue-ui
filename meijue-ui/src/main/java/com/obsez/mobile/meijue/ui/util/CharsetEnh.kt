package com.obsez.mobile.meijue.ui.util

import java.nio.charset.Charset


/**
 * 追加更多的字符集预置值
 */
object CharsetsEnh {
    @JvmField
    val UTF8 = Charset.forName("utf-8")
    
    @JvmField
    val GB18030: Charset = Charset.forName("GB18030")
}

