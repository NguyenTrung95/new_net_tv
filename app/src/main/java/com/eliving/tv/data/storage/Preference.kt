package com.eliving.tv.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.eliving.tv.AppContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences Utils
 */
class Preference<T>(private val key: String, private val defaultValue: T) : ReadWriteProperty<Any, T> {

    companion object {
        private const val SHARE_PRE_NAME = "open_wallet_pfkey"

        private val mPreferences: SharedPreferences by lazy {
            AppContext.getSharedPreferences(
                SHARE_PRE_NAME,
                Context.MODE_PRIVATE
            )
        }

        fun clear() {
            mPreferences.edit().clear()
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = findPreference(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = putPreference(key, value)

    private fun findPreference(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is Int -> mPreferences.getInt(key, defaultValue)
            is Long -> mPreferences.getLong(key, defaultValue)
            is Boolean -> mPreferences.getBoolean(key, defaultValue)
            is String -> mPreferences.getString(key, defaultValue)
            is Float -> mPreferences.getFloat(key, defaultValue)
            else -> throw IllegalArgumentException("This type can't be saved into SharedPreferences")
        } as T
    }

    private fun putPreference(key: String, value: T) {
        with(mPreferences.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                else -> throw IllegalArgumentException("This type can't be saved into SharedPreferences")
            }
        }.apply()
    }
}