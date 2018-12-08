package com.messy.util

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class Tip(context: Activity, msg: CharSequence) : PopupWindow() {
    private var view: View? = null
    private lateinit var observer: LifecycleObserver

    init {
        try {
            contentView = View.inflate(context, R.layout.tip, null)
            view = ActivityCompat.requireViewById(context, android.R.id.content)
            animationStyle = R.style.TipAnim
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            contentView.findViewById<TextView>(R.id.tip_msg).text = msg
            view.takeIf {
                it?.context is LifecycleOwner
            }?.apply {
                val owner = this.context as LifecycleOwner
                observer = getObserver()
                owner.lifecycle.addObserver(observer)
            }
        } catch (e: Exception) {
            catchThrowable(e)
        }
    }

    fun show() {
        val dy = view?.let { (it.context.displayHeight * 0.092).toInt() + it.context.statusBarHeight }
            ?: 0
        view?.post { showAtLocation(view, Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, dy) }
        view?.postDelayed({ dismiss() }, 2000L)
    }

    private fun getObserver(): LifecycleObserver {
        return object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun destroy() {
                if (isShowing) dismiss()
                view?.apply { (context as LifecycleOwner).lifecycle.removeObserver(observer) }
            }
        }
    }
}
