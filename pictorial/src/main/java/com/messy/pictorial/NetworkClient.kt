package com.messy.pictorial

import com.google.gson.Gson
import com.messy.util.lazyNone
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient private constructor() {
    companion object {

        private val lock = Any()
        private var instance: NetworkClient? = null
        @Suppress("SpellCheckingInspection")
        private var gson: Gson? = null

        fun getInstance(): NetworkClient {
            if (instance == null) {
                synchronized(lock) {
                    if (instance == null)
                        instance = NetworkClient()
                }
            }
            return instance!!
        }

        @Suppress("SpellCheckingInspection")
        fun getGson(): Gson {
            if (gson == null) {
                synchronized(lock) {
                    if (gson == null)
                        gson = Gson()
                }
            }
            return gson!!
        }

        fun clear() {
            instance?.clear()
            instance = null
            gson = null
        }
    }

    private val map by lazyNone { hashMapOf<String, Retrofit?>() }
    private val map2 by lazyNone { hashMapOf<String, Any?>() }
    private var okHttpClient: OkHttpClient? = null
    private var retrofitBuilder: Retrofit.Builder? = null

    fun clear() {
        map.clear()
        map2.clear()
        okHttpClient = null
        retrofitBuilder = null
    }

    fun <T> create(baseUrl: String, clazz: Class<T>): T {
        return getRetrofitClient(baseUrl).create(clazz)
    }

    fun <T> get(baseUrl: String, clazz: Class<T>): T {
        if (map2[baseUrl] == null) {
            map2[baseUrl] = getRetrofitClient(baseUrl).create(clazz) as Any
        }
        @Suppress("UNCHECKED_CAST")
        return map2[baseUrl]!! as T
    }

    private fun getRetrofitClient(baseUrl: String): Retrofit {
        if (map[baseUrl] == null) {
            map[baseUrl] = getRetrofitBuilder().baseUrl(baseUrl).build()
        }
        return map[baseUrl]!!
    }

    private fun getRetrofitBuilder(): Retrofit.Builder {
        if (retrofitBuilder == null)
            retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                /*.addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .client(getOkHttpClient())
        return retrofitBuilder!!
    }

    private fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(OneUA())
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            okHttpClient = builder.build()
        }
        return okHttpClient!!
    }
}