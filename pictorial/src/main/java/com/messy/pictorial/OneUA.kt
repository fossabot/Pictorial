package com.messy.pictorial

import okhttp3.Interceptor
import okhttp3.Response

class OneUA : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().removeHeader("User-Agent").addHeader(
                "User-Agent",
                "android-async-http/2.0 (http://loopj.com/android-async-http)"
            ).build()
        )
    }
}