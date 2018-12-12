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

    companion object {
        const val LEFT = 1 shl 0
        const val RIGHT = 1 shl 1
        const val UP = 1 shl 2
        const val DOWN = 1 shl 3
        const val SOLO = 1 shl 4
        const val ALL = LEFT or RIGHT or UP or DOWN
    }

    private val callback by lazy { Callback() }
    private val viewDragHelper = ViewDragHelper.create(this, callback)
    private var listener: DragEventListener? = null
    private var disListener: DragDistanceChangeListener? = null
    //releasedChild: View, xvel: Float, yvel: Float
    private var dragCondition: (View, Float, Float) -> Boolean =
        { _: View, _: Float, _: Float -> false }
    //changedView: View, left: Int, top: Int, dx: Int, dy: Int
    private var positionCondition: (View, Int, Int, Int, Int) -> Boolean =
        { _: View, _: Int, _: Int, _: Int, _: Int -> false }
    var dragDirection = ALL
    var dragLimitFactor = 0.1f
    private var curDirection = ALL
    fun setDragEventListener(listener: DragEventListener) {
        this.listener = listener
    }

    fun setDragEventListener(listener: () -> Unit) {
        this.listener = object : DragEventListener {
            override fun onEvent() {
                listener()
            }
        }
    }

    fun setDragDistanceChangeListener(listener: DragDistanceChangeListener) {
        disListener = listener
    }

    fun setDragDistanceChangeListener(listener: (Float) -> Unit) {
        disListener = object : DragDistanceChangeListener {
            override fun onDistanceChange(factor: Float) {
                listener(factor)
            }
        }
    }


    fun setDragFinishedCondition(block: (View, Float, Float) -> Boolean) {
        dragCondition = block
    }

    fun setDragPositionCondition(block: (View, Int, Int, Int, Int) -> Boolean) {
        positionCondition = block
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
        private val current = Point()
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
            current.x = left
            current.y = top
            val a = sqrt(((top - origin.y) * (top - origin.y) + (left - origin.x) * (left - origin.x)).toFloat())
            val b =
                sqrt(((measuredHeight - origin.y) * (measuredHeight - origin.y) + (measuredHeight - origin.x) * (measuredHeight - origin.x)).toFloat())
            val factor = 1 - a / (b * 0.8f)
            if (background != null)
                background.alpha = (255 * factor).toInt()
            if (dragDirection == ALL) {
                target.scaleX = factor
                target.scaleY = factor
            }
            /*val r = if (top > origin.y) -1 else 1
            target.rotation = factor * 90 * r*/
            disListener?.onDistanceChange(factor)
            callEvent = positionCondition(changedView, left, top, dx, dy) ||
                    (!positionCondition(changedView, left, top, dx, dy) &&
                            (a >= b * dragLimitFactor))
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (releasedChild == target) {
                if (callEvent || dragCondition(releasedChild, xvel, yvel)) {
                    listener?.onEvent()
                    callEvent = false
                } else {
                    viewDragHelper.settleCapturedViewAt(origin.x, origin.y)
                    invalidate()
                    curDirection = dragDirection
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
            return when (dragDirection) {
                ALL, RIGHT or LEFT, RIGHT or LEFT or UP, RIGHT or LEFT or DOWN -> left
                RIGHT, RIGHT or UP, RIGHT or DOWN, RIGHT or UP or DOWN -> if (dx >= 0) left else current.x
                LEFT, LEFT or UP, LEFT or DOWN, LEFT or UP or DOWN -> if (dx <= 0) left else current.x
                else -> current.x
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return when (dragDirection) {
                ALL, UP or DOWN, UP or DOWN or LEFT, UP or DOWN or RIGHT -> top
                DOWN, DOWN or LEFT, DOWN or RIGHT, DOWN or LEFT or RIGHT -> if (dy >= 0) left else current.y
                UP, UP or LEFT, UP or RIGHT, UP or LEFT or RIGHT -> if (dy <= 0) left else current.y
                else -> current.y
            }
        }
    }

    interface DragEventListener {
        fun onEvent()
    }

    interface DragDistanceChangeListener {
        fun onDistanceChange(factor: Float)
    }
}