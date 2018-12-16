package com.messy.pictorial

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.messy.pictorial.swipebackhelper.SwipeBackHelper
import com.messy.util.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_settings)
        setTranslateBar()
        updateSwitch.isChecked = Settings.getInstance().isEnableUpdate
        notifySwitch.isChecked = Settings.getInstance().isForegroundService
        lastUpdateTimeRight.text = Config.lastUpdateTime
        updateTimeText.text = Config.updateTimeText
        updateTimeText.tag = Config.updateTime
        updateSwitch.setOnCheckedChangeListener { _, isChecked -> Settings.getInstance().isEnableUpdate = isChecked }
        notifySwitch.setOnCheckedChangeListener { _, isChecked ->
            Settings.getInstance().isForegroundService = isChecked
            if (!isChecked) {
                LockService.stopLockService(applicationContext)
            } else {
                LockService.startLockService(applicationContext)
            }
        }
        updateTime.setOnClickListener { selectUpdateTime() }
        userGuide.setOnClickListener { startActivity(Intent(this, Welcome::class.java)) }
        versionName.text = getApkInfoLocal(this).versionName
        checkUpdateButton.setOnClickListener {
            val popupWindow = wait()
            checkUpdate(this).observe(this, Observer { pair ->
                popupWindow.dismiss()
                if (!pair.first) {
                    val dialog = AlertDialog.Builder(this, R.style.Dialog)
                        .setTitle(string(R.string.check_update))
                        .setMessage(string(R.string.can_update))
                        .setPositiveButton(string(R.string.confirm)) { dialog, which ->
                            dialog.cancel()
                            val progressDialog = ProgressDialog(this, R.style.Dialog)
                            progressDialog.setTitle(string(R.string.update_now))
                            progressDialog.progress = 0
                            progressDialog.max = 100
                            progressDialog.show()
                            getApkRemote(applicationContext, pair.second.outputFile).observe(this, Observer { result ->
                                if (result.status) {
                                    if (result.progress == 100)
                                        progressDialog.cancel()
                                    progressDialog.progress = result.progress
                                } else {
                                    snack(string(R.string.download_wrong))
                                    progressDialog.cancel()
                                }
                            })
                        }
                        .setNegativeButton(string(R.string.cancel)) { dialog, which ->
                            dialog.cancel()
                        }
                        .create()
                    dialog.show()
                } else {
                    snack(string(R.string.cannot_update))
                }
            })

        }
    }

    private fun selectUpdateTime() {
        val customView = inflate(R.layout.update_time_select)
        val bottomMenu: BottomMenu = BottomMenu.Builder().setCustomView(customView).setNoFrame(true).build()
        (customView as ViewGroup).foreach {
            if (it is RadioButton && it.text == updateTimeText.text) {
                it.checked()
            }
        }
        (customView as RadioGroup).setOnCheckedChangeListener { group, checkedId ->
            val checkedButton = group[group.checkedIndex ?: 1] as RadioButton
            updateTimeText.text = checkedButton.text
            updateTimeText.tag = (checkedButton.tag as String).toInt()
            Config.updateTimeText = checkedButton.text as String
            Config.updateTime = (checkedButton.tag as String).toInt()
            bottomMenu.dismiss()
        }
        bottomMenu.show(supportFragmentManager, null)
    }

    private fun setTranslateBar() {
        statusBarView.setHeight(statusBarHeight)
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        window.decorView.systemUiVisibility = option
    }
}
