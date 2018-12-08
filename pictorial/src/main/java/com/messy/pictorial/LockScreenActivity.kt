package com.messy.pictorial

import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.messy.pictorial.mvvm.Activity
import com.messy.util.color
import kotlinx.android.synthetic.main.activity_lock_screen.*


class LockScreenActivity : Activity<ReadingViewModel>() {

    override fun getViewModelClass(): Class<ReadingViewModel> = ReadingViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)
        applyOption()
        viewModel.getSingle().observe(this, Observer {
            image.load(it.imageUrl)
            contentTitle.text = it.title
            time.text = it.lastUpdateDate
            val spannableString = SpannableString("\t\t\t\t" + it.forward)
            spannableString.setSpan(
                BackgroundColorSpan(color(R.color.colorLockTextBackground)),
                0,
                spannableString.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text.text = spannableString
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        applyOption()
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
