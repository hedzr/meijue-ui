package com.obsez.mobile.meijue.ui.ext

inline fun <reified T : Enum<T>> T.next(): T {
    val allValues = enumValues<T>()
    val currentIndex = allValues.indexOf(this)
    val nextIndex = if (currentIndex + 1 >= allValues.size) 0 else currentIndex + 1
    return allValues[nextIndex]
}