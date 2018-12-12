package com.messy.pictorial

import android.content.Intent
import android.os.Bundle
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
import com.messy.pictorial.model.daydream.Story
import com.messy.pictorial.mvvm.Activity
import com.messy.util.removeInParent
import com.messy.util.setHeight
import com.messy.util.setItemAnimation
import com.messy.util.statusBarHeight
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity<StoryViewModel>() {

    private lateinit var adapter: ViewAdapter<Story, ViewHolder>

    override fun getViewModelClass(): Class<StoryViewModel> = StoryViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTranslateBar()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider, null))
        recyclerView.addItemDecoration(decoration)
        val transitionOptions = DrawableTransitionOptions.withCrossFade(200)
        adapter = ViewAdapter(R.layout.read_item)
        adapter.setOnBindListener { viewHolder, story ->
            viewHolder.apply {
                load(R.id.image, story.imgUrl, null, transitionOptions)
                transition(R.id.image, story.storyId)
            }
        }
        adapter.setOnItemClickListener { view, position ->
            val intent = Intent(this, PreviewActivity::class.java)
            val imageView = view.findViewById<View>(R.id.image)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(imageView, imageView.transitionName)
            ).toBundle()!!
            intent.putExtra("extra", adapter.data[position])
            startActivity(intent, bundle)
        }
        adapter.enablePreLoad(recyclerView, 3)
        adapter.setOnPreLoadListener { viewModel.nextPage() }
        recyclerView.adapter = adapter
        recyclerView.setItemAnimation(R.anim.read_item_enter, 0.15f, DecelerateInterpolator())
        var first = true
        viewModel.getStroies().observe(this, Observer {
            adapter.addAll(it)
            if (first) {
                first = false
                recyclerView.scheduleLayoutAnimation()
                progressBar.removeInParent()
            }
        })
        LockService.startLockService(this)
        if (Config.isFirstRun) {
            startActivity(Intent(this, Welcome::class.java))
            Config.isFirstRun = false
        }
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

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}
