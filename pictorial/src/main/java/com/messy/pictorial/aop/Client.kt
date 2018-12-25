package com.messy.pictorial.aop

class Client : IClient {

    lateinit var data: Data

    private val interceptors: MutableList<Interceptor> = mutableListOf()

    override fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    override fun start() {
        getRealInterceptorChain().process(data)
    }

    private fun getRealInterceptorChain(): RealInterceptorChain {
        interceptors.add(getLastInterceptor())
        return RealInterceptorChain(interceptors, 0, data)
    }

    private fun getLastInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Data {
                return chain.data()
            }

        }
    }
}