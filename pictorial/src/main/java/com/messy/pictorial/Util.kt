@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.messy.pictorial

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.IdRes
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.messy.adapter.ViewHolder

fun ImageView.clear() {
    GlideApp.with(this).clear(this)
}

fun ImageView.load(
    obj: Any?,
    option: RequestOptions? = null,
    listener: RequestListener<Drawable>? = null,
    translation: TransitionOptions<*, in Drawable>? = null
) {
    val builder = GlideApp.with(this).load(obj)
    if (option != null)
        builder.apply(option)
    if (listener != null)
        builder.listener(listener)
    if (translation != null)
        builder.transition(translation)
    builder.into(this)
}

fun ViewHolder.load(
    @IdRes viewId: Int, obj: Any?,
    option: RequestOptions? = null,
    translation: TransitionOptions<*, in Drawable>? = null
) {
    view<ImageView>(viewId).clear()
    view<ImageView>(viewId).load(obj, option, null, translation)
}
