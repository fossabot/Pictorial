package com.messy.pictorial.aop

interface Interceptor {
    fun intercept(chain: Chain): Data
    interface Chain {
        fun data(): Data
        fun process(data: Data): Data
    }
}