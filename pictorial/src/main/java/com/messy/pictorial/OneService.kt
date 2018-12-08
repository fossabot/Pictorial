package com.messy.pictorial

import androidx.lifecycle.LiveData
import com.messy.pictorial.model.read.ReadResult
import retrofit2.http.GET
import retrofit2.http.Path

interface OneService {

    companion object {
        private const val BASE_URL = "http://v3.wufazhuce.com:8000/api/"
        fun get() = NetworkClient.getInstance().get(BASE_URL, OneService::class.java)
        fun create() = NetworkClient.getInstance().create(BASE_URL, OneService::class.java)
    }

    @GET("channel/reading/more/{id}")
    fun readMore(@Path("id") id: String): LiveData<ReadResult>
}