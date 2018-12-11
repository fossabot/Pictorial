package com.messy.pictorial

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.messy.util.inflate
import com.messy.util.setHeight
import com.messy.util.statusBarHeight
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTranslateBar()
        updateSwitch.isChecked = Settings.getInstance().isEnableUpdate
        notifySwitch.isChecked = Settings.getInstance().isForegroundService
        lastUpdateTimeRight.text = Config.lastUpdateTime

        updateSwitch.setOnCheckedChangeListener { _, isChecked -> Settings.getInstance().isEnableUpdate = isChecked }
        notifySwitch.setOnCheckedChangeListener { _, isChecked ->
            Settings.getInstance().isForegroundService = isChecked
            if (!isChecked) {
                LockService.stopLockService(applicationContext)
            } else {
                LockService.startLockService(applicationContext)
            }
        }
        lastUpdateTime.setOnClickListener {
            val bottomMenu: BottomMenu =
                BottomMenu.Builder().setCustomView(inflate(R.layout.update_time_select)).build()
            bottomMenu.show(supportFragmentManager, null)
        }
    }

    private fun setTranslateBar() {
        statusBarView.setHeight(statusBarHeight)
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        window.decorView.systemUiVisibility = option
    }
}
