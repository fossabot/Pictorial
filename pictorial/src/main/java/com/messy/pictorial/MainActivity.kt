package com.messy.pictorial

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.messy.adapter.ViewAdapter
import com.messy.adapter.ViewHolder
import com.messy.pictorial.model.read.Reading
import com.messy.pictorial.mvvm.Activity
import com.messy.util.color
import com.messy.util.setHeight
import com.messy.util.setItemAnimation
import com.messy.util.statusBarHeight
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity<ReadingViewModel>() {

    private lateinit var adapter: ViewAdapter<Reading, ViewHolder>

    override fun getViewModelClass(): Class<ReadingViewModel> = ReadingViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTranslateBar()
        setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider, null))
        recyclerView.addItemDecoration(decoration)
        val transitionOptions = DrawableTransitionOptions.withCrossFade(200)
        adapter = ViewAdapter(R.layout.read_item)
        adapter.setOnBindListener { viewHolder, reading ->
            viewHolder.apply {
                val spannableString = SpannableString(reading.forward)
                spannableString.setSpan(
                    BackgroundColorSpan(color(R.color.colorLockTextBackground)),
                    0,
                    spannableString.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                text(R.id.text, spannableString)
                load(R.id.image, reading.imageUrl, null, transitionOptions)
                transition(R.id.image, reading.id)
                transition(R.id.text, reading.id + "text")
            }
        }
        adapter.setOnItemClickListener { view, position ->
            val intent = Intent(this, PreviewActivity::class.java)
            val imageView = view.findViewById<View>(R.id.image)
            val textView = view.findViewById<View>(R.id.text)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(imageView, imageView.transitionName),
                Pair(textView, textView.transitionName),
                Pair(view, "unused")
            ).toBundle()!!
            intent.putExtra("reading", adapter.data[position])
            startActivity(intent, bundle)
        }
        adapter.enablePreLoad(recyclerView, 3)
        adapter.setOnPreLoadListener {
            viewModel.nextPage(adapter.data[adapter.data.size - 1].id)
        }
        recyclerView.adapter = adapter
        recyclerView.setItemAnimation(R.anim.read_item_enter, 0.15f, DecelerateInterpolator())
        var first = true
        viewModel.getReadMore().observe(this, Observer {
            /*val result = DiffUtil.calculateDiff(ReadingCallback(adapter.data, it), true)
            result.dispatchUpdatesTo(adapter)
            adapter.data.addAll(it)
            */
            adapter.addAll(it)
            if (first) {
                first = false
                recyclerView.scheduleLayoutAnimation()
            }
        })
        LockService.startLockService(this)
    }

    private fun setTranslateBar() {
        statusBarView.setHeight(statusBarHeight)
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        window.decorView.systemUiVisibility = option
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return true
    }
}
