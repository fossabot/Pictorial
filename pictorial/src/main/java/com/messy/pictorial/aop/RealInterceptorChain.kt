package com.messy.pictorial.aop

class RealInterceptorChain(
    private val interceptors: List<Interceptor>,
    private val index: Int,
    private val lastData: Data
) : Interceptor.Chain {

    override fun data(): Data {
        return lastData
    }

    override fun process(data: Data): Data {
        if (index >= interceptors.size)
            return lastData
        val next = RealInterceptorChain(interceptors, index + 1, data)
        return interceptors[index].intercept(next)
    }

}