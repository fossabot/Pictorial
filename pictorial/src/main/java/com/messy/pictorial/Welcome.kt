package com.messy.pictorial

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.messy.pictorial.swipebackhelper.SwipeBackHelper
import com.messy.util.*
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {

    private val swipeBackHelper = SwipeBackHelper()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val consume = swipeBackHelper.progressTouchEvent(ev)
        return if (!consume)
            super.dispatchTouchEvent(ev)
        else
            false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContentView(R.layout.activity_welcome)
        text.text = htmlToText()
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun htmlToText(): CharSequence {
        val html = assets.getString(USER_NEED_TO_KNOW)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val type = intent?.data?.getQueryParameter(QUERY_TYPE_KEY)
        when (type) {
            CHECK_BOOT -> {
                if (checkCanReceiveBootCompleted()) {
                    snack("当前已获取开机启动权限")
                } else {
                    snack("当前未获取开机启动权限")
                }
            }
            CHECK_BACKGROUND_RUN -> {
                if (checkCanBackgroundRun()) {
                    snack("当前已获取后台运行权限")
                } else {
                    snack("当前未获取后台运行权限")
                }
            }
            CHECK_LOCK_SCREEN -> {
                if (checkCanShowOnLockScreen()) {
                    snack("当前已获取锁屏显示权限")
                } else {
                    snack("当前未获取锁屏显示权限")
                }
            }
            CHECK_INTERNET -> {
                if (checkInternetPermission()) {
                    snack("当前已获取网络权限")
                } else {
                    snack("当前未获取网络权限")
                }
            }
            CHECK_STORAGE -> {
                if (checkCanReadAndWrite()) {
                    snack("当前已获取SD卡读取权限")
                } else {
                    snack("当前未获取SD卡读取权限")
                }
            }
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    companion object {
        private const val USER_NEED_TO_KNOW = "user_need_to_know.html"
        private const val QUERY_TYPE_KEY = "type"
        private const val CHECK_BOOT = "check-boot"
        private const val CHECK_BACKGROUND_RUN = "check-background-run"
        private const val CHECK_LOCK_SCREEN = "check-lock-screen"
        private const val CHECK_INTERNET = "check-internet"
        private const val CHECK_STORAGE = "check-storage"
    }
}
