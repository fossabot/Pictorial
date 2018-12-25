package com.messy.pictorial.aop

interface IClient {
    fun start()
    fun addInterceptor(interceptor: Interceptor)
}