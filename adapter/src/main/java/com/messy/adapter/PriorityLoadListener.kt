package com.messy.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PriorityLoadListener(val context: Context) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                Glide.with(context).resumeRequests()
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                Glide.with(context).pauseRequests()
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                Glide.with(context).pauseRequests()
            }
        }
    }
}