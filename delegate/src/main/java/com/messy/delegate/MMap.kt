package com.messy.delegate

import android.app.Application
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MMap<T>(val default: T) : ReadWriteProperty<Any?, T> {
    companion object {
        private const val DEFAULT_NAME = "default_name"
        private val mmkv by lazy { MMKV.mmkvWithID(DEFAULT_NAME, MMKV.MULTI_PROCESS_MODE) }
        fun init(app: Application) {
            MMKV.initialize(app)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = property.name
        @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
        return when (default) {
            is Int -> mmkv.decodeInt(name, default)
            is Long -> mmkv.decodeLong(name, default)
            is String -> mmkv.decodeString(name, default)
            is Boolean -> mmkv.decodeBool(name, default)
            is Float -> mmkv.decodeFloat(name, default)
            else -> throw RuntimeException("Unsupported type.")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val key = property.name
        when (value) {
            is Long -> mmkv.encode(key, value)
            is String -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            else -> throw RuntimeException("Unsupported type.")
        }
    }

}
