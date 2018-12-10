package com.messy.pictorial.model.daydream

import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport

data class Story(
    @SerializedName("content_bgcolor")
    val contentBgcolor: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("pic_info")
    val picInfo: String = "",
    @SerializedName("id")
    val storyId: String = "",
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
    val author: Author,
    @SerializedName("words_info")
    val wordsInfo: String = "",
    @SerializedName("volume")
    val volume: String = "",
    @SerializedName("img_url")
    val imgUrl: String = "",
    @SerializedName("post_date")
    val postDate: String = "",
    @SerializedName("share_url")
    val shareUrl: String = "",
    @SerializedName("subtitle")
    val subtitle: String = ""
) : LitePalSupport()