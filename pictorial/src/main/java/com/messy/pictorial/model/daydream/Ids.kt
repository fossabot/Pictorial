package com.messy.pictorial.model.daydream

import com.google.gson.annotations.SerializedName

data class Ids(
    @SerializedName("res")
    val res: Int = 0,
    @SerializedName("data")
    val data: List<String>
)