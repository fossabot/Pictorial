package com.messy.pictorial

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.messy.pictorial.mvvm.TimerLiveData
import java.text.SimpleDateFormat
import java.util.*

class DateViewModel(application: Application) : AndroidViewModel(application) {

    private val timerLiveData = TimerLiveData({ Date() }, 1000 * 60)

    fun getTime(): LiveData<Date> {
        return timerLiveData
    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayDate(format: String): String {
        return SimpleDateFormat(format).format(Date())
    }
}