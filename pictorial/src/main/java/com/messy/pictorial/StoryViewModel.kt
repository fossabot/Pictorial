package com.messy.pictorial

import android.annotation.SuppressLint
import android.app.Application
import android.os.SystemClock
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.messy.pictorial.model.daydream.Story
import com.messy.pictorial.thread.ThreadPool
import java.text.SimpleDateFormat
import java.util.*

class StoryViewModel(application: Application) : AndroidViewModel(application) {
    private val storyRepository = StoryRepository()

    private var stories = MediatorLiveData<List<Story>>()

    private var single = MediatorLiveData<Story>()

    @MainThread
    fun getStories(): LiveData<List<Story>> {
        val source = storyRepository.stories()
        stories.addSource(source) {
            stories.postValue(it)
            stories.removeSource(source)
        }
        return stories
    }

    @MainThread
    fun getSingle(): LiveData<Story> {
        val source = storyRepository.single()
        single.addSource(source) {
            single.postValue(it)
            single.removeSource(source)
        }
        return single
    }

    fun nextPage() {
        val source = storyRepository.nextPage()
        if (source != null) {
            stories.addSource(source) {
                stories.postValue(it)
                stories.removeSource(source)
            }
        }
    }

    fun getTime(block: () -> Boolean): LiveData<Date> {
        val liveData = MediatorLiveData<Date>()
        ThreadPool.getDefaultThreadPool().execute {
            while (block()) {
                liveData.postValue(Date())
                SystemClock.sleep(1000 * 60)
            }
        }
        return liveData
    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayDate(format: String): String {
        return SimpleDateFormat(format).format(Date())
    }
}