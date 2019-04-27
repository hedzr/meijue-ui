package com.obsez.mobile.meijue.ui.ext

import android.content.Context
import com.obsez.mobile.meijue.ui.MeijueUi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmName

// kotlin-for-android-developers
// https://wangjiegulu.gitbooks.io/kotlin-for-android-developers-zh/zen_yao_qu_chuang_jian_yi_ge_zi_ding_yi_de_wei_tuo.html

object DelegatesExt {
    /**
     * 只能被赋值一次，多次赋值将产生异常
     */
    fun <T> notNullSingleValue() = NotNullSingleValueVar<T>()
    
    /**
     * d
     */
    fun <T> preference(name: String, default: T) = Preference(name, default)
}

//inline fun <reified T> Context.pref(name: String, default: T) = Preference(this, name, default)


class NotNullSingleValueVar<T> {
    
    private var value: T? = null
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        value ?: throw IllegalStateException("${property.name} not initialized")
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}

/*class Preference<T>(private val context: Context, private val name: String,
                    private val default: T) {
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, default)
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }
    
    @Suppress("UNCHECKED_CAST")
    private fun findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        
        res as T
    }
    
    @SuppressLint("CommitPrefEdits")
    private fun putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }.apply()
    }
}*/

class Preference<T>(val name: String, private val default: T, private val prefName: String = "default") : ReadWriteProperty<Any?, T> {
    
    constructor(default: T, prefName: String = "default") : this("", default, prefName)
    
    private val prefs by lazy { MeijueUi.get().context.getSharedPreferences(prefName, Context.MODE_PRIVATE) }
    
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(findProperName(property), default)
    }
    
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(findProperName(property), value)
    }
    
    private fun findProperName(property: KProperty<*>) = if (name.isEmpty()) property.name else name
    
    @Suppress("UNCHECKED_CAST")
    private fun <U> findPreference(name: String, default: U): U = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("Unsupported type")
        }
        res as U
    }
    
    private fun <U> putPreference(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }.apply()
    }
}


inline fun <reified R, T> R.pref(default: T) = Preference(R::class.jvmName, default)
//object Settings {
//    var lastPage by pref(0)
//}

