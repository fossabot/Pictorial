package com.messy.pictorial.model.read

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.litepal.crud.LitePalSupport

@Parcelize
data class Tag(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = ""
) : LitePalSupport(), Parcelable