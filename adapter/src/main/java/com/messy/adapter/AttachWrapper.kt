package com.messy.adapter

import androidx.recyclerview.widget.RecyclerView


interface AttachWrapper<VH : RecyclerView.ViewHolder> {
    fun onAttachToWrapperAdapter(wrapper: AdapterWrapper<VH>)
}