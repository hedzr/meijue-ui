package com.obsez.mobile.meijue.ui.ext


import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.v4.app.BundleCompat
import android.util.Size
import android.util.SizeF
import java.util.*


operator fun Bundle.contains(key: String): Boolean = containsKey(key)


inline fun <reified T> Bundle.obtain(key: String, noinline converter: ((Any) -> T?) = { it as? T }): T? = converter(this[key])


operator fun Bundle.set(key: String, value: Boolean): Boolean = value.apply { putBoolean(key, value) }

operator fun Bundle.set(key: String, value: Int): Int = value.apply { putInt(key, value) }

operator fun Bundle.set(key: String, value: Long): Long = value.apply { putLong(key, value) }

operator fun Bundle.set(key: String, value: Float): Float = value.apply { putFloat(key, value) }

operator fun Bundle.set(key: String, value: Double): Double = value.apply { putDouble(key, value) }

operator fun Bundle.set(key: String, value: Byte): Byte = value.apply { putByte(key, value) }

operator fun Bundle.set(key: String, value: Short): Short = value.apply { putShort(key, value) }

operator fun Bundle.set(key: String, value: Char): Char = value.apply { putChar(key, value) }

operator fun Bundle.set(key: String, value: CharSequence): CharSequence = value.apply { putCharSequence(key, value) }

operator fun Bundle.set(key: String, value: Bundle): Bundle = value.apply { putBundle(key, value) }

operator fun Bundle.set(key: String, value: Parcelable): Parcelable = value.apply { putParcelable(key, value) }

operator fun Bundle.set(key: String, value: IBinder): IBinder = value.apply { BundleCompat.putBinder(this@set, key, value) }

operator fun Bundle.set(key: String, value: Size): Size = value.apply { putSize(key, value) }

operator fun Bundle.set(key: String, value: SizeF): SizeF = value.apply { putSizeF(key, value) }


operator fun Bundle.set(key: String, value: Array<Boolean>): Array<Boolean> = value.apply { putBooleanArray(key, value.toBooleanArray()) }

operator fun Bundle.set(key: String, value: Array<Int>): Array<Int> = value.apply { putIntArray(key, value.toIntArray()) }

operator fun Bundle.set(key: String, value: Array<Long>): Array<Long> = value.apply { putLongArray(key, value.toLongArray()) }

operator fun Bundle.set(key: String, value: Array<Float>): Array<Float> = value.apply { putFloatArray(key, value.toFloatArray()) }

operator fun Bundle.set(key: String, value: Array<Double>): Array<Double> = value.apply { putDoubleArray(key, value.toDoubleArray()) }

operator fun Bundle.set(key: String, value: Array<Byte>): Array<Byte> = value.apply { putByteArray(key, value.toByteArray()) }

operator fun Bundle.set(key: String, value: Array<Short>): Array<Short> = value.apply { putShortArray(key, value.toShortArray()) }

operator fun Bundle.set(key: String, value: Array<Char>): Array<Char> = value.apply { putCharArray(key, value.toCharArray()) }

operator fun Bundle.set(key: String, value: Array<String>): Array<String> = value.apply { putStringArray(key, value) }

operator fun Bundle.set(key: String, value: Array<CharSequence>): Array<CharSequence> = value.apply { putCharSequenceArray(key, value) }


@JvmName("putIntegerArrayList")
operator fun Bundle.set(key: String, value: ArrayList<Int>): ArrayList<Int> = value.apply { putIntegerArrayList(key, value) }

@JvmName("putStringArrayList")
operator fun Bundle.set(key: String, value: ArrayList<String>): ArrayList<String> = value.apply { putStringArrayList(key, value) }

@JvmName("putCharSequenceArrayList")
operator fun Bundle.set(key: String, value: ArrayList<CharSequence>): ArrayList<CharSequence> = value.apply { putCharSequenceArrayList(key, value) }

@JvmName("putParcelableArrayList")
operator fun Bundle.set(key: String, value: ArrayList<Parcelable>): ArrayList<Parcelable> = value.apply { putParcelableArrayList(key, value) }

