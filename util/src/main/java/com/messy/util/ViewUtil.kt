@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.messy.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.LayoutAnimationController
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.annotation.AnimRes
import androidx.annotation.Px
import androidx.core.view.*
import androidx.recyclerview.widget.RecyclerView

inline fun View.setHeight(height: Int) {
    setSize(width, height)
}

inline fun View.setWidth(width: Int) {
    setSize(width, height)
}

fun View.setSize(width: Int, height: Int) {
    var layoutParams = this.layoutParams
    if (layoutParams == null) {
        layoutParams = ViewGroup.LayoutParams(width, height)
    } else {
        layoutParams.width = width
        layoutParams.height = height
    }
    this.layoutParams = layoutParams
}

inline fun View.visible(bool: Boolean = true) {
    visibility = if (bool) View.VISIBLE else View.GONE
}

inline fun View.gone(bool: Boolean = true) {
    visibility = if (bool) View.GONE else View.VISIBLE
}

inline fun View.inVisible(bool: Boolean = true) {
    visibility = if (bool) View.INVISIBLE else View.VISIBLE
}

inline fun View.enable() {
    isEnabled = true
}

inline fun View.unEnable() {
    isEnabled = false
}

inline fun CompoundButton.checked() {
    isChecked = true
}

inline fun CompoundButton.unChecked() {
    isChecked = false
}


inline fun View.setMargins(@Px marginLeft: Int, @Px marginTop: Int, @Px marginRight: Int, @Px marginBottom: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(marginLeft, marginTop, marginRight, marginBottom)
}

inline fun View.setMarginLeft(@Px marginLeft: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(marginLeft, marginTop, marginRight, marginBottom)
}

inline fun View.setMarginTop(@Px marginTop: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(marginLeft, marginTop, marginRight, marginBottom)
}

inline fun View.setMarginRight(@Px marginRight: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(marginLeft, marginTop, marginRight, marginBottom)
}

inline fun View.setMarginBottom(@Px marginBottom: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(marginLeft, marginTop, marginRight, marginBottom)
}

inline fun View.setPaddingLeft(@Px paddingLeft: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

inline fun View.setPaddingTop(@Px paddingTop: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

inline fun View.setPaddingRight(@Px paddingRight: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

inline fun View.setPaddingBottom(@Px paddingBottom: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}


inline operator fun ViewGroup.get(index: Int): View = this.getChildAt(index)

inline val ViewGroup.first: View
    get() = this.getChildAt(0)
inline val ViewGroup.last: View
    get() = this.getChildAt(this.size - 1)

val RadioGroup.checkedIndex: Int?
    get() {
        forEachIndexed { index, v -> takeIf { v.id == checkedRadioButtonId }?.apply { return index } }
        return null
    }

fun RecyclerView.setItemAnimation(
    @AnimRes id: Int, delay: Float = 0.1f,
    interpolator: Interpolator = LinearInterpolator(),
    order: Int = LayoutAnimationController.ORDER_NORMAL
) {
    val animation = AnimationUtils.loadAnimation(context, id)
    val layoutAnimController = LayoutAnimationController(animation)
    layoutAnimController.interpolator = interpolator
    layoutAnimController.order = order
    layoutAnimController.delay = delay
    layoutAnimation = layoutAnimController
}

inline fun View?.removeInParent() {
    (this?.parent as? ViewGroup)?.removeView(this)
}

inline val View?.parentView: ViewGroup?
    get() {
        return (this?.parent as? ViewGroup)
    }

inline fun ViewGroup.foreach(block: (View) -> Unit) {
    for (i in 0 until childCount) {
        block(get(i))
    }
}

fun View.openKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.closeKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}