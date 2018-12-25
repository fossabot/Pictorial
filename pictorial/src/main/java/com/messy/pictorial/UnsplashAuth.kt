package com.messy.pictorial

import okhttp3.Interceptor
import okhttp3.Response

class UnsplashAuth : Interceptor {
    companion object {
        private const val ACCESS_KEY = "0ed2f50d804ab3665cb9f034a216146af7f9d8732d2d3b4cf37dba7e91b92aa5"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val builderInterval = origin.newBuilder().method(origin.method(), origin.body())
            .addHeader("Authorization", "Client-ID $ACCESS_KEY")
            .addHeader("Accept-Version", "v1")
        return chain.proceed(builderInterval.build())
    }
}