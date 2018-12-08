package com.messy.adapter

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.messy.util.visible

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
open class ViewHolder(itemView: View, parent: ViewGroup, viewType: Int) :
    RecyclerView.ViewHolder(itemView) {

    @Suppress("UNCHECKED_CAST")
    fun <V : View> view(@IdRes viewId: Int): V {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as V
    }

    fun transition(@IdRes viewId: Int, transition: String) {
        view<View>(viewId).transitionName = transition
    }

    fun transition(@IdRes viewId: Int): String? {
        return view<View>(viewId).transitionName
    }

    fun text(@IdRes viewId: Int, text: String) {
        view<TextView>(viewId).text = text
    }

    fun text(@IdRes viewId: Int, text: SpannableString) {
        view<TextView>(viewId).text = text
    }

    fun text(@IdRes viewId: Int, text: () -> String) {
        view<TextView>(viewId).text = text()
    }

    fun text(@IdRes viewId: Int): CharSequence? {
        return view<TextView>(viewId).text
    }

    fun check(@IdRes viewId: Int, isChecked: Boolean) {
        view<CheckBox>(viewId).isChecked = isChecked
    }

    fun check(@IdRes viewId: Int): Boolean {
        return view<CheckBox>(viewId).isChecked
    }


    fun visible(@IdRes viewId: Int, isVisible: Boolean = true) {
        view<View>(viewId).visible(isVisible)
    }

    fun onCheckedChange(@IdRes viewId: Int, block: (button: CompoundButton, isChecked: Boolean) -> Unit) {
        view<CheckBox>(viewId).setOnCheckedChangeListener(block)
    }

    fun onClick(@IdRes viewId: Int, block: (view: View) -> Unit) {
        if (!view<View>(viewId).hasOnClickListeners())
            view<View>(viewId).setOnClickListener(block)
    }

    fun background(@IdRes viewId: Int, @ColorInt color: Int) {
        view<View>(viewId).setBackgroundColor(color)
    }

    fun background(@IdRes viewId: Int, drawable: Drawable) {
        view<View>(viewId).background = drawable
    }

    protected val views: SparseArray<View> by lazy { SparseArray<View>() }
}