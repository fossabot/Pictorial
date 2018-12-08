package com.messy.pictorial

import com.messy.pictorial.model.read.Reading
import org.litepal.LitePal

class ReadingModel {
    fun saveAll(list: List<Reading>) {
        list.forEach(::save)
    }

    fun save(item: Reading) {
        item.author?.save()
        item.shareInfo?.save()
        item.save()
    }

    fun update(item: Reading) {
        item.clearSavedState()
        item.save()
    }

    fun find(id: String): Reading? {
        return LitePal.where("id = ?", id).find(Reading::class.java, true)[0]
    }

    fun findNext(id: String): Reading? {
        return LitePal.where("id > ? limit 1", id).find(Reading::class.java, true)[0]
    }

    fun findAll(): List<Reading> {
        return LitePal.findAll(Reading::class.java, true) ?: emptyList()
    }
}