package com.messy.pictorial

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.messy.pictorial.model.read.Reading

class ReadingViewModel(application: Application) : AndroidViewModel(application) {

    private val readingRepository = ReadingRepository()

    private var readingData = MediatorLiveData<List<Reading>>()

    private var singleData = MediatorLiveData<Reading>()

    @MainThread
    fun getReadMore(): LiveData<List<Reading>> {
        val more = readingRepository.readMore()
        readingData.addSource(more) {
            readingData.postValue(it)
            readingData.removeSource(more)
        }
        return readingData
    }

    @MainThread
    fun getSingle(): LiveData<Reading> {
        val single = readingRepository.readSingle()
        singleData.addSource(single) {
            singleData.postValue(it)
            singleData.removeSource(single)
        }
        return singleData
    }

    fun nextPage(id: String) {
        val more = readingRepository.readMore(id)
        readingData.addSource(more) {
            readingData.postValue(it)
            readingData.removeSource(more)
        }
    }
}