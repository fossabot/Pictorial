package com.messy.pictorial.model.read

import com.google.gson.annotations.SerializedName

data class ReadResult(@SerializedName("res")
                      val res: Int = 0,
                      @SerializedName("data")
                      val data: List<Reading>?)