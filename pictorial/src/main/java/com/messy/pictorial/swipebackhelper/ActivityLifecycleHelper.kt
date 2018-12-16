package com.messy.pictorial.swipebackhelper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.messy.util.logd
import java.util.*

class ActivityLifecycleHelper : Application.ActivityLifecycleCallbacks {

    private val activities = LinkedList<Activity>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activities.add(activity)
        Log.d("ALH", "addActivity")
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        activities.remove(activity)
        Log.d("ALH", "removeActivity")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
    }

    fun getPreviousActivity(): Activity? {
        Log.d("ALH", "size =${activities.size}")

        return if (activities.size < 2) null
        else activities[activities.size - 2]
    }

    fun getCurrentActivity(): Activity? {
        Log.d("ALH", "size =${activities.size}")

        return if (activities.size == 0) null
        else activities[activities.size - 1]
    }

}