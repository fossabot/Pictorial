package com.messy.pictorial

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.sqrt

class DragView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private val callback = Callback()
    private val viewDragHelper = ViewDragHelper.create(this, callback)
    private var listener: DragEventListener? = null

    fun setDragEventListener(listener: DragEventListener) {
        this.listener = listener
    }

    fun setDragEventListener(listener: () -> Unit) {
        this.listener = object : DragEventListener {
            override fun onEvent() {
                listener.invoke()
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
        super.computeScroll()
    }


    private inner class Callback : ViewDragHelper.Callback() {
        private val origin = Point()
        private var callEvent = true
        private lateinit var target: View
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (child == getChildAt(0)) {
                target = child
                origin.x = target.left
                origin.y = target.top
                return true
            }
            return false
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            val a = sqrt(((top - origin.y) * (top - origin.y) + (left - origin.x) * (left - origin.x)).toFloat())
            val b = sqrt(((measuredHeight - origin.y) * (measuredHeight - origin.y) + (measuredHeight - origin.x) * (measuredHeight - origin.x)).toFloat())
            val factor = 1 - a / (b * 0.8f)
            background.alpha = (255 * factor).toInt()
            target.scaleX = factor
            target.scaleY = factor
            /*val r = if (top > origin.y) -1 else 1
            target.rotation = factor * 90 * r*/
            callEvent = a > b * 0.2
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (releasedChild == target) {
                if (callEvent || yvel > 8000) {
                    listener?.onEvent()
                    callEvent = false
                } else {
                    viewDragHelper.settleCapturedViewAt(origin.x, origin.y)
                    invalidate()
                }
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 1
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }
    }

    interface DragEventListener {
        fun onEvent() {

        }
    }
}