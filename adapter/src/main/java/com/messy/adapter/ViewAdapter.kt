@file:Suppress("unused")

package com.messy.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.messy.util.inflate
import com.messy.util.linearLayoutManager
import com.messy.util.logd
import java.lang.reflect.Constructor

class ViewAdapter<T, VH : ViewHolder>(
    @LayoutRes private val layoutResId: Int, val data: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<VH>(), AttachWrapper<VH> {

    constructor(
        @LayoutRes layoutResId: Int,
        data: MutableList<T> = mutableListOf(),
        @Suppress("UNCHECKED_CAST")
        clazz: Class<VH>
    ) : this(layoutResId, data) {
        this.clazz = clazz
    }

    @Suppress("MemberVisibilityCanBePrivate")
    var wrapper: AdapterWrapper<VH>? = null
    var canLoadMore = true
    var preLoadLimitSize = 1

    private var clazz: Class<VH>? = null
    private var cons: Constructor<VH>? = null
    private var onItemClick: ((view: View, position: Int) -> Unit)? = null
    private lateinit var onBind: (VH, T) -> Unit
    private var onPreLoad: (() -> Unit)? = null
    private var isPreLoading = false
    private var enablePreLoad = false
    private var preLoadListener: PreLoadListener? = null
    private var observer: PreLoadDataObserver? = null

    override fun onAttachToWrapperAdapter(wrapper: AdapterWrapper<VH>) {
        this.wrapper = wrapper
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = parent.context.inflate(layoutResId, parent)
        if (cons == null) {
            if (clazz == null)
                clazz = ViewHolder::class.java as Class<VH>
            //itemView: View, parent: ViewGroup, viewType: Int
            cons = clazz!!.getConstructor(View::class.java, ViewGroup::class.java, Int::class.java)
        }
        val holder = cons!!.newInstance(itemView, parent, viewType)
        itemView.setOnClickListener {
            onItemClick?.invoke(it, holder.layoutPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBind(holder, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnBindListener(listener: (VH, T) -> Unit) {
        onBind = listener
    }

    fun setOnItemClickListener(listener: (view: View, position: Int) -> Unit) {
        onItemClick = listener
    }

    fun replaceAll(list: List<T>) {
        data.clear()
        data.addAll(0, list)
        notifyDataSetChanged()
    }

    fun addAll(list: List<T>) {
        val start = data.size
        data.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }

    fun enablePreLoad(recyclerView: RecyclerView, size: Int = preLoadLimitSize) {
        if (size <= 0)
            return
        enablePreLoad = true
        preLoadLimitSize = size
        if (preLoadListener == null)
            preLoadListener = PreLoadListener()
        recyclerView.addOnScrollListener(preLoadListener!!)
        if (observer == null)
            observer = PreLoadDataObserver()
        this.registerAdapterDataObserver(observer!!)
        logd("addPreLoadListener")
    }

    fun closePreLoad(recyclerView: RecyclerView) {
        if (!enablePreLoad)
            return
        enablePreLoad = false
        if (observer != null) {
            this@ViewAdapter.unregisterAdapterDataObserver(observer!!)
            observer = null
        }
        if (preLoadListener != null) {
            recyclerView.removeOnScrollListener(preLoadListener!!)
            preLoadListener = null
        }
    }

    fun setOnPreLoadListener(listener: () -> Unit) {
        onPreLoad = listener
    }

    private inner class PreLoadDataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            logd("data onChanged")
            isPreLoading = false
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            logd("data onItemRangeInserted")
            isPreLoading = false
        }
    }

    private inner class PreLoadListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (recyclerView.layoutManager !is LinearLayoutManager)
                return

            val lastVisibleItemPosition = recyclerView.linearLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = recyclerView.linearLayoutManager.itemCount
            logd(
                "lastVisibleItemPosition=$lastVisibleItemPosition totalItemCount=$totalItemCount" +
                        "preLoadLimitSize=$preLoadLimitSize isPreLoading=$isPreLoading canLoadMore=$canLoadMore"
            )
            if (!canLoadMore) {
                this@ViewAdapter.unregisterAdapterDataObserver(observer!!)
                recyclerView.removeOnScrollListener(preLoadListener!!)
                observer = null
                preLoadListener = null
            }

            if (lastVisibleItemPosition >= totalItemCount - preLoadLimitSize && !isPreLoading && dy > 0 && canLoadMore) {
                isPreLoading = true
                onPreLoad?.invoke()
            }
        }
    }

}