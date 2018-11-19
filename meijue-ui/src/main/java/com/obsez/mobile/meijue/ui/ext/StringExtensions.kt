@file:JvmName("StringUtils")

package com.obsez.mobile.meijue.ui.ext

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Patterns
import com.bumptech.glide.Glide
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


fun String.toRadix31Integer(): Int = Radix31.stringToJava31radix(this)

fun Int.toRadix31String(): String = Radix31.intToJava31radix(this)


inline fun <reified T : CharSequence> String.ptn(map: Map<String, T>): CharSequence {
    return """\{(.*?)}""".toRegex().replace(this) { mr ->
        // println(mr.groupValues[1] + " position: [" + mr.range + "]")
        if (map.containsKey(mr.groupValues[1])) map[mr.groupValues[1]]!! else mr.value
    }
}


fun String.htmlToSpan(context: Context, imageGetter: Html.ImageGetter = Html.ImageGetter {
    Glide.with(context).load(it).submit(-1, -1).get()
}): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this, imageGetter, null)
    }
}




