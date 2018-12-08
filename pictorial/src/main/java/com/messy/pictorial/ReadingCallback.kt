package com.messy.pictorial

import androidx.recyclerview.widget.DiffUtil
import com.messy.pictorial.model.read.Reading

class ReadingCallback(private val old: List<Reading>, private val new: List<Reading>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].id == new[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }
}