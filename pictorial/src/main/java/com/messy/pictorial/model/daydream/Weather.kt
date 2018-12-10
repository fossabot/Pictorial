package com.messy.pictorial.model.daydream

import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport

data class Weather(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("city_name")
    val cityName: String = "",
    @SerializedName("temperature")
    val temperature: String = "",
    @SerializedName("humidity")
    val humidity: String = "",
    @SerializedName("wind_direction")
    val windDirection: String = "",
    @SerializedName("hurricane")
    val hurricane: String = "",
    @SerializedName("climate")
    val climate: String = "",
    @SerializedName("icons")
    val icons: Icons
) : LitePalSupport()