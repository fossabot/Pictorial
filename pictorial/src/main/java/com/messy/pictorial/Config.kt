package com.messy.pictorial

import com.messy.delegate.MMap
import java.text.SimpleDateFormat
import java.util.*

object Config {
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.CHINA)
    var currentItem: String by MMap("")
    var lastItem: String by MMap("")
    var lastUpdateTime: String by MMap(dateFormat.format(Date()))
    var isFirstRun: Boolean by MMap(true)
    var updateTimeText: String by MMap("2小时")
    var updateTime: Int by MMap(120)
    fun updateTime() {
        lastUpdateTime = dateFormat.format(Date())
    }

    fun isNeedToUpdate(currentTime: Date = Date(), limit: Long = 2 * 60L): Boolean {
        val lastTimeMS = try {
            dateFormat.parse(lastUpdateTime).time
        } catch (e: Exception) {
            Date().time
        }
        val currentTimeMS = currentTime.time
        val diff = (currentTimeMS - lastTimeMS) / (1000 * 60)
        return diff >= limit
    }
}