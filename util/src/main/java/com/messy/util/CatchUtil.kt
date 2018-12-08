@file:Suppress("unused")

package com.messy.util

import android.util.Log

private var isPrintCE = true

fun setPrintCatchException(b: Boolean) {
  isPrintCE = b
}

private fun isPrintCatchException(): Boolean {
  return isPrintCE
}

fun printException(e: Throwable? = null, tag2: String? = null) {
  Log.e("${if (tag2 != null) "$tag2 " else ""}catchThrowable", null, e)
}

fun catchThrowable(e: Throwable? = null, isPrint: Boolean? = null, tag2: String? = null) {
  if (isPrint != null && isPrint || isPrint == null && isPrintCatchException())
    printException(e, tag2)
}

fun catch(isPrint: Boolean? = null, tag2: String? = null, block: () -> Unit) {
  try {
    block.invoke()
  } catch (e: Exception) {
    catchThrowable(e, isPrint, tag2)
  }
}