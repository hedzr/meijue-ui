@file:JvmName("StringUtils")

package com.obsez.mobile.meijue.ui.ext

import android.support.annotation.StyleRes
import android.text.Spannable
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Patterns
import com.obsez.mobile.meijue.ui.util.Radix31
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.Normalizer
import kotlin.experimental.and

// from toshi

//fun String.isGroupId(): Boolean {
//    // To-do - check compatability with other clients (i.e. iOS)
//    return length == GROUP_ID_LENGTH
//}

fun String.isWebUrl() = Patterns.WEB_URL.matcher(this.trim()).matches()

fun String.findTypeParamValue(): String? {
    val regexResult = Regex("type=([a-zA-Z]+)", RegexOption.IGNORE_CASE)
            .find(this)
    if (regexResult?.groups?.size != 2) return null
    return regexResult.groups[1]?.value
}

fun String.getQueryMap(): HashMap<String, String> {
    val minLength = 3
    if (this.length < minLength || !this.contains("=")) return HashMap()
    val params = this.split("&")
    val map = HashMap<String, String>()
    for (param in params) {
        val stringSplitter = param.split("=")
        val name = stringSplitter[0]
        val value = stringSplitter[1]
        map[name] = value
    }
    return map
}

fun String.safeToInt(): Int {
    return try {
        this.toInt()
    } catch (e: NumberFormatException) {
        return 0
    }
}

/**
 * Normalize string - convert to lowercase, replace diacritics and trim trailing whitespaces
 */
fun String.normalize(): String {
    return Normalizer.normalize(toLowerCase(), Normalizer.Form.NFD)
            .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "").trim()
}


fun String.fromBase64(): String? {
    return try {
        String(this.toByteArray(Charsets.UTF_8).fromBase64(), Charsets.UTF_8)
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        null
    }
}

fun String.toBase64(): String? {
    return try {
        this.toByteArray(Charsets.UTF_8).toBase64()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        null
    }
}


fun ByteArray.fromBase64(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, Base64.DEFAULT)
}


fun String.md5(): String {
    try {
        val md5 = MessageDigest.getInstance("MD5")
        val bytes = md5.digest(this.toByteArray(Charsets.UTF_8))
        var result = ""
        for (b in bytes) {
            var temp = Integer.toHexString(b.and(0xFF.toByte()).toInt())
            if (temp.length == 1) {
                temp = "0$temp"
            }
            result += temp
        }
        return result
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    
    return ""
}

fun String.sha1(): String {
    try {
        val sha1 = MessageDigest.getInstance("SHA-1")
        val bytes = sha1.digest(this.toByteArray(Charsets.UTF_8))
        var result = ""
        for (b in bytes) {
            val temp = Integer.toHexString(b.and(0xFF.toByte()).toInt())
            if (temp.length < 2) {
                result += "0"
            }
            result += temp
        }
        return result
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    
    return ""
}


fun String.toRadix31Integer(): Int {
    return Radix31.stringToJava31radix(this)
}

fun Int.toRadix31String(): String {
    return Radix31.intToJava31radix(this)
}


/**
 * Highlight substring [query] in this spannable with custom [style]. All occurrences of this substring
 * are stylized
 */
fun Spannable.highlightSubstring(query: String, @StyleRes style: Int): Spannable {
    val spannable = Spannable.Factory.getInstance().newSpannable(this)
    val substrings = query.toLowerCase().split("\\s+".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
    var lastIndex = 0
    for (i in substrings.indices) {
        do {
            lastIndex = this.toString().toLowerCase().indexOf(substrings[i], lastIndex)
            if (lastIndex != -1) {
                spannable.setSpan(StyleSpan(style), lastIndex, lastIndex + substrings[i].length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                lastIndex++
            }
        } while (lastIndex != -1)
    }
    return spannable
}


