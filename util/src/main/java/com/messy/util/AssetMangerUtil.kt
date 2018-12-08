@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.messy.util

import android.content.res.AssetManager
import java.nio.charset.Charset

fun AssetManager.getString(filePath: String) =
  open(filePath).use { it.readBytes().toString(Charset.defaultCharset()) }
