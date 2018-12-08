@file:Suppress("unused")

package com.messy.util

import androidx.viewpager.widget.ViewPager


fun ViewPager.addOnPageScrollStateChangedListener(block: (state: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            block(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }

    })
}

fun ViewPager.addOnPageScrolledListener(block: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            block(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {

        }

    })
}

fun ViewPager.addOnPageSelectedListener(block: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            block(position)
        }

    })
}

fun ViewPager.addOnPageChangeListener(
    block1: (state: Int) -> Unit,
    block2: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit,
    block3: (position: Int) -> Unit
) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            block1(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            block2(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            block3(position)
        }

    })
}
