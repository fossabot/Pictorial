package com.messy.pictorial.aop.demo

import com.messy.pictorial.aop.Data
import com.messy.pictorial.aop.Interceptor

class ThirdInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Data {
        val data = chain.data() as StringData
        println("Third Before string=[${data.string}] ver=[${data.version}]")
        val newData = StringData(data.string + "->third", data.version + 1)
        val result = try {
            chain.process(newData) as StringData
        } catch (e: Exception) {
            StringData()
        }
        println("Third After string=[${result.string}] ver=[${result.version}]")
        return result
    }

}