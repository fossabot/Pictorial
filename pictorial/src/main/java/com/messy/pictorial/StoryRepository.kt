package com.messy.pictorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.messy.pictorial.model.daydream.Story
import com.messy.pictorial.thread.ThreadPool

class StoryRepository {
    private val oneService by lazy { OneService.create() }

    private var ids: List<String>? = null
    private var page = 0

    private fun oneDay(id: String = "0"): LiveData<List<Story>> {
        val data = MediatorLiveData<List<Story>>()
        data.addSource(oneService.oneDay(id)) {
            //ThreadPool.getDefaultThreadPool().execute { data.postValue(StoryModel.findAll()) }
            data.postValue(it.data.contentList)
            ThreadPool.getDefaultThreadPool().execute {
                Config.updateTime()
                StoryModel.saveAll(it.data.contentList)
            }
        }
        return data
    }

    fun nextPage(): LiveData<List<Story>>? {
        return if (++page < 10) stories() else null
    }

    fun stories(): LiveData<List<Story>> {
        return Transformations.switchMap(oneService.getIds()) {
            ids = it.data
            oneDay(it.data[page])
        }
    }

    fun single(): LiveData<Story> {
        return Transformations.map(stories()) {
            it[0]
        }
    }
}