@file:Suppress("unused")

package com.messy.util

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment

fun Context.Dp2Px(dp: Int) =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

fun Fragment.Dp2Px(dp: Int) =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

fun Context.Px2Dp(px: Int) =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), resources.displayMetrics).toInt()

fun Fragment.Px2Dp(px: Int) =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), resources.displayMetrics).toInt()

fun Context.Sp2Px(sp: Int) =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics)

fun Fragment.Sp2Px(sp: Int) =
  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics)
