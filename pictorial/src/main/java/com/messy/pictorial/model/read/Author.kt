package com.messy.pictorial.model.read

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.litepal.crud.LitePalSupport
@Parcelize
data class Author(@SerializedName("summary")
                  val summary: String = "",
                  @SerializedName("wb_name")
                  val wbName: String = "",
                  @SerializedName("web_url")
                  val webUrl: String = "",
                  @SerializedName("user_id")
                  val userId: String = "",
                  @SerializedName("user_name")
                  val userName: String = "",
                  @SerializedName("fans_total")
                  val fansTotal: String = "",
                  @SerializedName("is_settled")
                  val isSettled: String = "",
                  @SerializedName("settled_type")
                  val settledType: String = "",
                  @SerializedName("desc")
                  val desc: String = ""):LitePalSupport(), Parcelable