package com.messy.pictorial

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.messy.pictorial.mvvm.Activity
import com.messy.util.displayHeight
import com.messy.util.inVisible
import com.messy.util.string
import com.messy.util.visible
import kotlinx.android.synthetic.main.activity_lock_screen.*


class LockScreenActivity : Activity<StoryViewModel>() {

    private var isResume = false
    private var isLoad = false
    override fun getViewModelClass(): Class<StoryViewModel> = StoryViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)
        applyOption()
        isLoad = false
        isResume = false
        dragView.dragDirection = DragView.RIGHT
        dragView.dragLimitFactor = 0.15f
        dragView.setDragEventListener { finish() }
        val height = displayHeight * 0.2f
        dragView.setDragDistanceChangeListener {
            val translationY = -it * height + height
            text.translationY = translationY
            contentTitle.translationY = translationY
            info.translationY = translationY
            time.translationY = translationY
            line.translationY = translationY
        }
        viewModel.getSingle().observe(this, Observer {
            image.load(it.imgUrl)
            contentTitle.text = it.title
            time.text = it.lastUpdateDate
            text.text = it.forward
            val inf = StringBuilder()
            if (it.picInfo.isNotEmpty())
                inf.append("@").append(string(R.string.pic_info, it.wordsInfo))
            else if (it.author.userName.isNotEmpty())
                inf.append("@").append(string(R.string.pic_info, it.author.userName))
            if (it.wordsInfo.isNotEmpty())
                inf.append(" ").append(string(R.string.word_info, it.wordsInfo))
            info.text = inf.toString()
            isLoad = true
            startAnim()
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        applyOption()
    }

    override fun onResume() {
        super.onResume()
        isResume = true
        startAnim()
    }

    private fun startAnim() {
        if (isResume && isLoad) {
            lockScreen.inVisible()
            TransitionManager.beginDelayedTransition(lockScreen)
            lockScreen.visible()
            isResume = false
        }
    }

    private fun applyOption() {
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }
}
