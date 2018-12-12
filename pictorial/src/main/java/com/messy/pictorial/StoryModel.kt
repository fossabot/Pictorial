package com.messy.pictorial

import com.messy.pictorial.model.daydream.Story
import org.litepal.LitePal

class StoryModel {
    companion object {
        fun saveAll(list: List<Story>) {
            list.forEach(::save)
        }

        fun save(item: Story) {
            item.author.save()
            item.save()
        }

        fun update(item: Story) {
            item.clearSavedState()
            item.save()
        }

        fun find(id: String): Story? {
            return LitePal.where("storyId = ?", id).find(Story::class.java, true)[0]
        }

        fun findNext(id: String): Story? {
            return LitePal.where("storyId > ? limit 1", id).find(Story::class.java, true)[0]
        }

        fun findAll(): List<Story> {
            return LitePal.findAll(Story::class.java, true) ?: emptyList()
        }
    }

}