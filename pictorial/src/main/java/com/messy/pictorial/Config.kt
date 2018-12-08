package com.messy.pictorial

import com.messy.delegate.MMap
import java.text.SimpleDateFormat
import java.util.*

object Config {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.CHINA)
    var currentItem: String by MMap("")
    var lastItem: String by MMap("")
    var lastUpdateTime: String by MMap(dateFormat.format(Date()))

    fun isNeedToUpdate(currentTime: Date = Date(), limit: Long = 2 * 60L): Boolean {
        val lastTimeMS = dateFormat.parse(lastUpdateTime).time
        val currentTimeMS = currentTime.time
        val diff = (currentTimeMS - lastTimeMS) / (1000 * 60)
        return diff >= limit
    }
}