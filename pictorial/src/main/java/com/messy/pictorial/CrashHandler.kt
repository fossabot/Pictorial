package com.messy.pictorial

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import com.messy.util.string
import com.messy.util.toast
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        private var crashHandler: CrashHandler? = null
        private val lock = Any()
        fun getInstance(): CrashHandler {
            if (crashHandler == null)
                synchronized(lock) {
                    if (crashHandler == null)
                        crashHandler = CrashHandler()
                }
            return crashHandler!!
        }

    }

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    private lateinit var application: Application

    private lateinit var message: HashMap<String, String>

    fun init(application: Application) {
        this.application = application
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (!handlerException(t, e)) {
            defaultHandler?.uncaughtException(t, e)
        } else {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {

            }
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
            application.startActivity(Intent(application, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    private fun handlerException(t: Thread?, e: Throwable?): Boolean {
        if (e == null) return false
        thread {
            Looper.prepare()
            application.toast(application.string(R.string.crash_tip))
            Looper.loop()
        }
        try {
            collectMessage()
            saveMessage(e)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    private fun saveMessage(e: Throwable) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Device Info:\n")
        message.forEach {
            stringBuilder.append("[").append(it.key).append("]").append("=")
                .append("[").append(it.value).append("]").append("\n")
        }
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        stringBuilder.append("Crash Info:").append(stringWriter.toString())
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
        val fileName = "crash-report-" + time + "-" + System.currentTimeMillis() + ".log"
        val filePath = application.externalCacheDir!!.canonicalPath + "/crash/"
        val crashDir = File(filePath)
        if (!crashDir.exists()) crashDir.mkdirs()
        val fos = FileOutputStream(filePath + fileName)
        fos.use {
            it.write(stringBuilder.toString().toByteArray())
        }
    }

    private fun collectMessage() {
        message = HashMap()
        val packageManager = application.packageManager
        val packageInfo = packageManager.getPackageInfo(application.packageName, PackageManager.GET_ACTIVITIES)
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.versionCode.toString()
        message["versionName"] = versionName
        message["versionCode"] = versionCode
        val fields = Build::class.java.fields
        if (fields.isNotEmpty()) {
            fields.forEach {
                it.isAccessible = true
                message[it.name] = it.get(null).toString()
                it.isAccessible = false
            }
        }
    }
}