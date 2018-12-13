package com.messy.pictorial

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Fade
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.messy.pictorial.model.daydream.Story
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {
    companion object {
        private const val HIDE_ANIM_MILLIS = 400L
        private const val UI_ANIMATION_DELAY = 400L
        private const val AUTO_HIDE_UI_MILLIS = 3000L
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
        //TransitionManager.beginDelayedTransition(controls.parent as ViewGroup, fade)
        controls.visibility = View.VISIBLE
    }
    private val hideRunnable = Runnable { /*hide()*/ }
    private var mVisible: Boolean = true
    private var isClearTransFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.navigationBarColor = Color.TRANSPARENT
        isClearTransFlag = true
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        setContentView(R.layout.activity_preview)
        mVisible = true
        photoView.minimumScale = 1f
        photoView.maximumScale = 3.2f
        dragView.setDragFinishedCondition { _, xvel, yvel -> yvel > 3600 || xvel > 3600 }
        dragView.setDragEventListener { supportFinishAfterTransition() }
        dragView.dragLimitFactor = 0.16f
        dragView.setDragDistanceChangeListener {
            controls.translationY = -it * controls.height + controls.height
        }
        photoView.setOnClickListener { toggle() }
        photoView.minimumScale = 1f
        val reading = intent!!.getParcelableExtra<Story>("extra")
        photoView.transitionName = reading.storyId
        text.text = reading.forward
        postponeEnterTransition()
        val listener = requestListener()
        photoView.load(reading.imgUrl, listener = listener)
        photoView.setOnScaleChangeListener { scaleFactor, focusX, focusY ->
            Log.d("PREVIEW_A", "scale =$scaleFactor fx=$focusX fy=$focusY")
        }
        photoView.postDelayed(hideRunnable, AUTO_HIDE_UI_MILLIS)
        photoView.postDelayed({
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.navigationBarColor = Color.TRANSPARENT
            isClearTransFlag = true
        }, AUTO_HIDE_UI_MILLIS + 500)
        // photoView.
    }

    private fun requestListener(): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
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
        //TransitionManager.beginDelayedTransition(controls.parent as ViewGroup, fade)
        controls.visibility = View.GONE
        mVisible = false
        mHideHandler.removeCallbacks(showControls)
        mHideHandler.postDelayed(hideControls, UI_ANIMATION_DELAY)
    }

    private fun show() {
        mHideHandler.removeCallbacks(hideControls)
        if (!isClearTransFlag) {
            window.navigationBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        // Show the system bar
        photoView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(hideRunnable)
        mHideHandler.postDelayed(showControls, UI_ANIMATION_DELAY)
        mHideHandler.postDelayed(hideRunnable, UI_ANIMATION_DELAY + AUTO_HIDE_UI_MILLIS)
    }

}
