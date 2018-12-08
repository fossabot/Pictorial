package com.messy.pictorial.model.read

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.litepal.crud.LitePalSupport

@Parcelize
data class Reading(
    @SerializedName("content_bgcolor")
    val colorBackground: String = "",
    @SerializedName("has_reading")
    val hasReading: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("content_type")
    val contentType: String = "",
    @SerializedName("tag_list")
    val tagList: List<Tag> = listOf(),
    @SerializedName("id")

    val id: String = "",
    @SerializedName("last_update_date")
    val lastUpdateDate: String = "",
    @SerializedName("like_count")
    val likeCount: Int = 0,
    @SerializedName("item_id")
    val itemId: String = "",
    @SerializedName("content_id")
    val contentId: String = "",
    @SerializedName("forward")
    val forward: String = "",
    @SerializedName("author")
    val author: Author? = null,
    @SerializedName("share_info")
    val shareInfo: ShareInfo? = null,
    @SerializedName("words_info")
    val wordsInfo: String = "",
    @SerializedName("audio_platform")
    val audioPlatform: Int = 0,
    @SerializedName("img_url")
    val imageUrl: String = "",
    //"2017-07-20 06:00:00"
    @SerializedName("post_date")
    val postDate: String = "",
    @SerializedName("share_url")
    val shareUrl: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("category")
    val category: Int = 1,
    @SerializedName("display_category")
    val displayCategory: Int = 1
): LitePalSupport(), Parcelable