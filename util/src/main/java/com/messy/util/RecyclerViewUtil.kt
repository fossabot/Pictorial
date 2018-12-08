package com.messy.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

inline val RecyclerView.gridLayoutManager: GridLayoutManager
    get() {
        return layoutManager as GridLayoutManager
    }

inline val RecyclerView.linearLayoutManager: LinearLayoutManager
    get() {
        return layoutManager as LinearLayoutManager
    }

inline val RecyclerView.staggeredGridLayoutManager: StaggeredGridLayoutManager
    get() {
        return layoutManager as StaggeredGridLayoutManager
    }