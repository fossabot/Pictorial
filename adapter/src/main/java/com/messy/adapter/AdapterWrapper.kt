package com.messy.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.messy.util.visible
import java.lang.reflect.Constructor

@Suppress("unused")
class AdapterWrapper<VH : RecyclerView.ViewHolder>(
    private val adapter: RecyclerView.Adapter<VH>
) : RecyclerView.Adapter<VH>() {


    constructor(
        adapter: RecyclerView.Adapter<VH>,
        @Suppress("UNCHECKED_CAST")
        clazz: Class<VH>
    ) : this(adapter) {
        this.clazz = clazz
    }

    init {
        if (adapter is AttachWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            adapter as AttachWrapper<VH>
            adapter.onAttachToWrapperAdapter(this)
        }
    }

    companion object {
        const val ITEM_TYPE_HEADER = 100000
        const val ITEM_TYPE_FOOTER = 200000
        const val ITEM_TYPE_EMPTY_VIEW = -1
    }

    private val headerViews = SparseArray<View>(4)

    private val footerViews = SparseArray<View>(4)

    private var emptyView: View? = null

    @Suppress("UNCHECKED_CAST")
    private var clazz: Class<VH> = ViewHolder::class.java as Class<VH>

    private var cons: Constructor<VH>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        headerViews.get(viewType)?.let {
            if (cons == null)
                cons = clazz.getConstructor(View::class.java)
            return cons!!.newInstance(it)
        }
        footerViews.get(viewType)?.let {
            if (cons == null)
                cons = clazz.getConstructor(View::class.java)
            return cons!!.newInstance(it)
        }
        if (viewType == ITEM_TYPE_EMPTY_VIEW) {
            if (cons == null)
                cons = clazz.getConstructor(View::class.java)
            return cons!!.newInstance(emptyView ?: View(parent.context))
        }
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isHeaderPosition(position) -> headerViews.keyAt(position)
            isFooterPosition(position) -> footerViews.keyAt(position - getHeaderCount() - getRealCount())
            isEmptyPosition(position) -> ITEM_TYPE_EMPTY_VIEW
            else -> adapter.getItemViewType(position - getRealCount())
        }
    }

    fun getRealCount() = adapter.itemCount

    fun getFooterCount(): Int = headerViews.size()

    fun getHeaderCount(): Int = footerViews.size()

    override fun getItemCount(): Int {
        return getHeaderCount() + getFooterCount() + getRealCount()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position))
            return
        if (isEmptyPosition(position)) {
            holder.itemView.visible(adapter.itemCount == 0)
            return
        }
        adapter.onBindViewHolder(holder, position)
    }

    fun addHeaderView(view: View) {
        headerViews.put(ITEM_TYPE_HEADER + getHeaderCount(), view)
    }

    fun addFooterView(view: View) {
        headerViews.put(ITEM_TYPE_FOOTER + getFooterCount(), view)
    }

    fun setEmptyView(view: View?) {
        emptyView = view
    }

    private fun isEmptyPosition(position: Int): Boolean {
        return position == getRealCount() + getHeaderCount()
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < getHeaderCount()
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= getRealCount() && position < getRealCount() + getHeaderCount()
    }

}