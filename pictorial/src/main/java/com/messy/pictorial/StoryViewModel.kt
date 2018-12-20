package com.messy.pictorial

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.messy.pictorial.model.daydream.Story

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

}