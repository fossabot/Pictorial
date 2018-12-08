package com.messy.pictorial.model.read

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.litepal.crud.LitePalSupport
@Parcelize
data class ShareInfo(
    @SerializedName("image")
    val imageUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("content")
    val content: String = ""
): LitePalSupport(), Parcelable