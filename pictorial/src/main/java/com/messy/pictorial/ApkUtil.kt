package com.messy.pictorial

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.messy.pictorial.model.apk.ApkInfo
import com.messy.pictorial.model.apk.ApkOutputInfo
import com.messy.pictorial.model.download.DownloadProgress
import com.messy.pictorial.thread.ThreadPool
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.io.File
import java.io.FileOutputStream

@Suppress("SpellCheckingInspection")
interface OneDrive {

    companion object {
        private const val BASE_URL = "https://github.com/microtears/Pictorial/blob/master/"
        fun create() = NetworkClient.getInstance().create(BASE_URL, OneDrive::class.java)
    }

    @GET("pictorial/build/output.json")
    fun getApkInfoRemote(): Call<ApkOutputInfo>

    @Streaming
    @GET("pictorial/build/{apkname}")
    fun getApkRemote(@Path("apkname") apkName: String): Call<ResponseBody>
}

private val oneDrive by lazy { OneDrive.create() }

private fun getApkInfoRemote(): ApkInfo {
    return try {
        val call = oneDrive.getApkInfoRemote()
        val response = call.execute()
        response.body()!!.apkInfo
    } catch (e: Exception) {
        ApkInfo()
    }
}

fun getApkInfoLocal(context: Context): ApkInfo {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return ApkInfo(
        versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        },
        versionName = packageInfo.versionName
    )
}

fun checkUpdate(context: Context): LiveData<Pair<Boolean, ApkInfo>> {
    val liveData = MutableLiveData<Pair<Boolean, ApkInfo>>()
    ThreadPool.getDefaultThreadPool().execute {
        try {
            val localApkInfo = getApkInfoLocal(context)
            val remoteApkInfo = getApkInfoRemote()
            val isNew = remoteApkInfo.versionCode > localApkInfo.versionCode
            liveData.postValue(Pair(isNew, remoteApkInfo))
        } catch (e: Exception) {
            liveData.postValue(Pair(false, getApkInfoLocal(context)))
        }
    }
    return liveData
}

fun getApkRemote(context: Context, apkName: String): LiveData<DownloadProgress> {
    val call = oneDrive.getApkRemote(apkName)
    val notify = MutableLiveData<DownloadProgress>()
    call.enqueue(object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            notify.postValue(DownloadProgress(status = false))
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            try {
                val body = response.body()!!
                val buffer = ByteArray(4096)
                val fileSize = body.contentLength()
                var downloadedSize = 0
                val pathname = context.cacheDir!!.canonicalPath!! + File.separator + apkName
                val inputStream = body.byteStream()
                val outputStream = FileOutputStream(File(pathname))
                var seek: Int
                outputStream.use {
                    inputStream.use {
                        do {
                            seek = inputStream.read(buffer)
                            if (seek <= 0)
                                break
                            outputStream.write(buffer, 0, seek)
                            downloadedSize += seek
                            notify.postValue(
                                DownloadProgress(
                                    progress = (downloadedSize * 100 / fileSize).toInt(),
                                    status = true,
                                    fileName = pathname,
                                    fileSize = fileSize
                                )
                            )
                        } while (true)
                        outputStream.flush()
                    }
                }
            } catch (e: java.lang.Exception) {
                notify.postValue(DownloadProgress(status = false))
            }
        }

    })
    return notify
}

fun installApk(context: Context, uri: Uri) {
    val installIntent = Intent()
    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    installIntent.action = Intent.ACTION_VIEW
    installIntent.setDataAndType(uri, "application/vnd.android.package-archive")
    context.startActivity(installIntent)
}