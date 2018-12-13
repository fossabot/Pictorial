package com.messy.pictorial.model.apk

import com.google.gson.annotations.SerializedName

data class ApkOutputInfo(
    @SerializedName("path")
    val path: String = "",
    @SerializedName("outputType")
    val outputType: OutputType,
    @SerializedName("apkInfo")
    val apkInfo: ApkInfo,
    @SerializedName("properties")
    val properties: Properties
)