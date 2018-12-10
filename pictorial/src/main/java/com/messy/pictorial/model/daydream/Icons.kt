package com.messy.pictorial.model.daydream

import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport

data class Icons(@SerializedName("night")
                 val night: String = "",
                 @SerializedName("day")
                 val day: String = ""): LitePalSupport()