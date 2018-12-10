package com.messy.pictorial.model.daydream

import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport

data class Daydream(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("content_list")
    val contentList: List<Daydream>,
    @SerializedName("weather")
    val weather: Weather,
    @SerializedName("id")
    val itemId: String = ""
) : LitePalSupport()