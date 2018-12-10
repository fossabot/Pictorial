package com.messy.pictorial.model.daydream

import com.google.gson.annotations.SerializedName

data class DaydreamResult(@SerializedName("res")
                        val res: Int = 0,
                          @SerializedName("data")
                        val data: Daydream)