@file:Suppress("unused")

package com.messy.util

import android.graphics.Color
import com.google.android.material.animation.ArgbEvaluatorCompat
import java.util.*

val random by lazy { Random() }
fun getColorEvaluate(start: Int, end: Int, percentage: Float): Int {
    return ArgbEvaluatorCompat.getInstance().evaluate(percentage, start, end) as Int
}


fun gtArr(limit: Int): ColorArray {
    return ColorArray { random.nextInt(256 - limit + 1) + limit }
}

fun ltArr(limit: Int): ColorArray {
    return ColorArray { random.nextInt(limit) }
}

fun bgArr(limit: Int): ColorArray {
    return ColorArray {
        random.nextInt(256).let { v -> if (v < limit) v + limit * (v / limit) else v }
    }
}

class ColorArray() {
    private val intArray = IntArray(3)

    constructor(init: (Int) -> Int) : this() {
        for (index in intArray.indices) {
            intArray[index] = init(index)
        }
    }


    operator fun get(index: Int): Int = intArray[index]
    operator fun set(index: Int, value: Int) {
        intArray[index] = value
    }

    fun color(alpha: Int = 255) = Color.argb(alpha, intArray[0], intArray[1], intArray[2])

    var r: Int
        get() = intArray[0]
        set(value) {
            intArray[0] = value
        }
    var g: Int
        get() = intArray[1]
        set(value) {
            intArray[1] = value
        }
    var b: Int
        get() = intArray[2]
        set(value) {
            intArray[2] = value
        }

}