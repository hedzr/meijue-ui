package com.obsez.mobile.meijue.ui.util

/**
 * radix 31 - decimal
 *
 * Created by hz on 2018/3/12.
 */

class Radix31(n: Int) {
    var number: Int = n
    
    override fun toString(): String {
        if (number == 0) return "0"
        if (number < 0) {
            val sb = StringBuffer()
            sb.append('-')
            sb.append(calc(-number))
            return sb.toString()
        }
        return calc(number)
    }
    
    fun toInt(str: String): Int {
        var n = 0
        for (ch in str) {
            if (ch in table) {
                n = n * radix + table.indexOf(ch)
            } else
                break
        }
        return n
    }
    
    companion object {
        private const val table: String = "0123456789ABCDEFGHJKMNPQRSTVWXY"
        private const val radix: Int = 31
        
        private fun calc(number: Int): String {
            var current = number
            val sb = StringBuffer()
            while (current != 0) {
                var remainder = current.rem(radix)
                sb.insert(0, table[remainder])
                current /= radix
            }
            return sb.toString()
        }
        
        fun intToJava31radix(i: Int): String {
            return i.toString(radix)
        }
        
        fun stringToJava31radix(s: String): Int {
            return s.toIntOrNull(radix) ?: -1
        }
    }
}
