package com.messy.pictorial

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.messy.pictorial.model.read.Reading
import com.messy.pictorial.thread.ThreadPool

class ReadingRepository {
    private val oneService by lazy { OneService.create() }
    private val readingModel = ReadingModel()

    fun readMore(id: String = "0"): LiveData<List<Reading>> {
        val data = MediatorLiveData<List<Reading>>()
        data.addSource(oneService.readMore(id)) {
            if (it.data != null) {
                data.postValue(it.data)
                ThreadPool.getDefaultThreadPool().execute { readingModel.saveAll(it.data) }
            } else if (id == "0") {
                ThreadPool.getDefaultThreadPool().execute { data.postValue(readingModel.findAll()) }
            }
        }
        return data
    }

    fun readSingle(): LiveData<Reading> {
        val single = MediatorLiveData<Reading>()
        val cur = Config.currentItem
        if (TextUtils.isEmpty(cur)) {
            val more = readMore()
            single.addSource(more) {
                Config.currentItem = it[0].id
                Config.lastItem = it[0].id
                single.postValue(it[0])
                single.removeSource(more)
            }
        } else {
            ThreadPool.getDefaultThreadPool().execute {
                if (Config.isNeedToUpdate()) {
                    val next = readingModel.findNext(Config.currentItem)
                    if (next != null) {
                        Config.lastItem = Config.currentItem
                        Config.currentItem = next.id
                        single.postValue(next)
                    }
                } else {
                    val result = readingModel.find(Config.currentItem)
                    if (result != null) {
                        single.postValue(result)
                    }
                }

            }
        }
        return single
    }
}