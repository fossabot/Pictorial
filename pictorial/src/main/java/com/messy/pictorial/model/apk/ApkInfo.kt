package com.messy.pictorial.model.apk

import com.google.gson.annotations.SerializedName

data class ApkInfo(
    @SerializedName("outputFile")
    val outputFile: String = "",
    @SerializedName("fullName")
    val fullName: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("versionName")
    val versionName: String = "",
    @SerializedName("baseName")
    val baseName: String = "",
    @SerializedName("versionCode")
    val versionCode: Long = 0,
    @SerializedName("enabled")
    val enabled: Boolean = false
)