package com.obsez.mobile.meijue.ui.ext

import android.content.SharedPreferences
import android.util.Base64

fun SharedPreferences.applyString(key: String, value: String?) = edit().putString(key, value).apply()

fun SharedPreferences.commitString(key: String, value: String?) = edit().putString(key, value).commit()

fun SharedPreferences.getString(key: String): String? = getString(key, null)

fun SharedPreferences.applyBoolean(key: String, value: Boolean = false) = edit().putBoolean(key, value).apply()

fun SharedPreferences.commitBoolean(key: String, value: Boolean = false) = edit().putBoolean(key, value).commit()

fun SharedPreferences.getBoolean(key: String): Boolean = getBoolean(key, false)

fun SharedPreferences.applyInt(key: String, value: Int = 0) = edit().putInt(key, value).apply()

fun SharedPreferences.commitInt(key: String, value: Int = 0) = edit().putInt(key, value).commit()

fun SharedPreferences.getInt(key: String): Int = getInt(key, 0)

fun SharedPreferences.applyClear() = edit().clear().apply()

fun SharedPreferences.applyByteArray(key: String, value: ByteArray) {
    val encoded = Base64.encodeToString(value, Base64.NO_WRAP)
    applyString(key, encoded)
}

fun SharedPreferences.getByteArray(key: String): ByteArray? {
    val encoded = getString(key) ?: return null
    return Base64.decode(encoded, Base64.NO_WRAP)
}