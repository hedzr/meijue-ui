@file:Suppress("NOTHING_TO_INLINE")

package com.obsez.mobile.meijue.ui.ext

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

//import org.jetbrains.anko.db.MapRowParser
//import org.jetbrains.anko.db.SelectQueryBuilder
//
//fun <T : Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
//        parseList(object : MapRowParser<T> {
//            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
//        })
//
//fun <T : Any> SelectQueryBuilder.parseOpt(parser: (Map<String, Any?>) -> T): T? =
//        parseOpt(object : MapRowParser<T> {
//            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
//        })
//
//fun SelectQueryBuilder.byId(id: Long) = whereSimple("_id = ?", id.toString())


inline fun Cursor.getString(columnName: String): String =
        getStringOrNull(columnName)!!

inline fun Cursor.getStringOrNull(columnName: String): String? {
    val index = getColumnIndexOrThrow(columnName)
    return if (isNull(index)) null else getString(index)
}


inline fun SQLiteDatabase.clear(tableName: String) {
    execSQL("delete from $tableName")
}

inline fun SQLiteDatabase.inTransaction(func: SQLiteDatabase.() -> Unit) {
    beginTransaction()
    try {
        func()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}


