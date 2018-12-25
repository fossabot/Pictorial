package com.messy.pictorial.aop.demo

import com.messy.pictorial.aop.Data
import com.messy.pictorial.aop.Interceptor

class SecondInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Data {
        val data = chain.data() as StringData
        println("Second Before string=[${data.string}] ver=[${data.version}]")
        val newData = StringData(data.string + "->second", data.version + 1)
        val result = chain.process(newData) as StringData
        println("Second After string=[${result.string}] ver=[${result.version}]")
        return result
    }

}