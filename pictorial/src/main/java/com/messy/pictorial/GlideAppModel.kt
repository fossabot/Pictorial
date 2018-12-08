package com.messy.pictorial

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class GlideAppModel : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        val memoryCacheSizeBytes = 1024 * 1024 * 20L // 20mb
        val bitmapPoolSizeBytes = 1024 * 1024 * 30L // 30mb
        val diskCacheSizeBytes = 1024 * 1024 * 30L//  30mb
        val cacheFolderName = "hut_helper"
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSizeBytes))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, cacheFolderName, diskCacheSizeBytes))
        if (BuildConfig.DEBUG)
            builder.setLogLevel(Log.DEBUG)
        else
            builder.setLogLevel(Log.INFO)
    }

    override fun isManifestParsingEnabled(): Boolean = false
}