package com.messy.pictorial

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class SquareImageView : ImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var measureSpec = widthMeasureSpec
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        when (mode) {
            MeasureSpec.EXACTLY -> {
            }
            MeasureSpec.AT_MOST -> {
            }
            MeasureSpec.UNSPECIFIED -> {
            }
        }
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                measureSpec = heightMeasureSpec
            }
            MeasureSpec.AT_MOST -> {
                measureSpec = heightMeasureSpec
            }
            MeasureSpec.UNSPECIFIED -> {
            }
        }
        super.onMeasure(measureSpec, measureSpec)
    }
}