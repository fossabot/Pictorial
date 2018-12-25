package com.messy.pictorial.aop.demo

import com.messy.pictorial.aop.Client

fun main(args: Array<String>) {

    val client = Client()
    client.addInterceptor(FirstInterceptor())
    client.addInterceptor(SecondInterceptor())
    client.addInterceptor(ThirdInterceptor())
    client.data = StringData()
    client.start()
}