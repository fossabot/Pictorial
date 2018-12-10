package com.messy.pictorial

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.messy.pictorial.model.read.Reading
import com.messy.util.displayHeight
import com.messy.util.displayWidth
import kotlinx.android.synthetic.main.activity_preview.*
import kotlin.math.min

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
        TransitionManager.beginDelayedTransition(fullscreen_content_controls.parent as ViewGroup, fade)
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = true
    private var isClearTransFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        mVisible = true
        photoView.minimumScale = 1f
        photoView.maximumScale = 10f
        dragView.setDragFinishedCondition { _, xvel, yvel -> yvel > 3600 || xvel > 3600 }
        dragView.setDragEventListener { supportFinishAfterTransition() }
        photoView.setOnClickListener { toggle() }
        photoView.minimumScale = 1f
        val reading = intent!!.getParcelableExtra<Reading>("reading")
        photoView.transitionName = reading.readingId
        text.transitionName = reading.readingId + "text"
        text.text = reading.forward
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
                if (resource != null) {
                    val intrinsicWidth = resource.intrinsicWidth
                    val intrinsicHeight = resource.intrinsicHeight
                    val width = displayWidth
                    val height = displayHeight
                    var scale = 1.0f
                    //如果图片宽度大于控件宽度，图片高度小于控件高度  图片缩小
                    if (intrinsicWidth > width && intrinsicHeight < height) {
                        scale = intrinsicWidth * 1.0f / width
                    }
                    //如果图片的高度大于控件的高度，图片的宽度小于控件的宽度  图片缩小
                    if (intrinsicHeight > height && intrinsicWidth < width) {
                        scale = intrinsicHeight * 1.0f / height
                    }
                    //如果图片的宽与高都大于控件的宽与高 或者 图片的宽与高都小于控件的宽与高
                    if ((intrinsicHeight > height && intrinsicWidth > width)) {
                        scale = min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight)
                    }
                    if (intrinsicHeight < height && intrinsicWidth < width) {
                        scale = min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight)
                    }
                    photoView.scale = scale
                }
                supportStartPostponedEnterTransition()
                return false
            }

        }
        photoView.load(reading.imageUrl, listener = listener)
        photoView.setOnScaleChangeListener { scaleFactor, focusX, focusY ->
            Log.d("PREVIEW_A", "scale =$scaleFactor fx=$focusX fy=$focusY")
        }
        photoView.postDelayed({ toggle() }, AUTO_HIDE_UI_MILLIS)
        photoView.postDelayed({
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.navigationBarColor = Color.TRANSPARENT
            isClearTransFlag = true
        }, AUTO_HIDE_UI_MILLIS + 500)
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
        mHideHandler.postDelayed(hideControls, UI_ANIMATION_DELAY)
    }

    private fun show() {
        if (!isClearTransFlag) {
            window.navigationBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        // Show the system bar
        photoView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(hideControls)
        mHideHandler.postDelayed(showControls, UI_ANIMATION_DELAY)
        mHideHandler.postDelayed({ hide() }, UI_ANIMATION_DELAY + AUTO_HIDE_UI_MILLIS)
    }

}
