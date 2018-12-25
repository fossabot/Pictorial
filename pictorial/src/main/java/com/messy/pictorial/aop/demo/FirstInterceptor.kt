package com.messy.pictorial.aop.demo

import com.messy.pictorial.aop.Data
import com.messy.pictorial.aop.Interceptor

class FirstInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Data {
        val data = chain.data() as StringData
        println("First Before string=[${data.string}] ver=[${data.version}]")
        val newData = StringData(data.string + "->first", data.version + 1)
        val result = chain.process(newData) as StringData
        println("First After string=[${result.string}] ver=[${result.version}]")
        return result
    }

}