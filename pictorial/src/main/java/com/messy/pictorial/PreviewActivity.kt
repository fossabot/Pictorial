package com.messy.pictorial

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.messy.pictorial.model.read.Reading
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {
    companion object {
        private const val HIDE_ANIM_MILLIS = 300L
        private const val UI_ANIMATION_DELAY = 300
    }

    private val fade = Fade().also { it.duration = HIDE_ANIM_MILLIS }
    private val mHideHandler = Handler()
    private val hideControls = Runnable {
        photoView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showControls = Runnable {
        TransitionManager.beginDelayedTransition(fullscreen_content_controls.parent as ViewGroup, fade)
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        mVisible = true
        /*photoView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
        dragView.setDragEventListener { supportFinishAfterTransition() }
        photoView.setOnClickListener { toggle() }
        val reading = intent!!.getParcelableExtra<Reading>("reading")
        photoView.transitionName = reading.id
        postponeEnterTransition()
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                supportStartPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                supportStartPostponedEnterTransition()
                return false
            }

        }
        photoView.load(reading.imageUrl, listener = listener)
        // photoView.

    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun onResume() {
        super.onResume()
        dragView.background.alpha = 255
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        TransitionManager.beginDelayedTransition(fullscreen_content_controls.parent as ViewGroup, fade)
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false
        mHideHandler.removeCallbacks(showControls)
        mHideHandler.postDelayed(hideControls, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        photoView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(hideControls)
        mHideHandler.postDelayed(showControls, UI_ANIMATION_DELAY.toLong())
    }

}
