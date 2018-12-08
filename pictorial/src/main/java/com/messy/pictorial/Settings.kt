package com.messy.pictorial

import com.messy.delegate.MMap

class Settings {
    companion object {
        private var settings: Settings? = null
        fun getInstance(): Settings {
            if (settings == null) {
                synchronized(this) {
                    if (settings == null) {
                        settings = Settings()
                    }
                }
            }
            return settings!!
        }
    }

    var isForegroundService by MMap(true)
    var isEnableUpdate by MMap(true)

}