package com.messy.util

import android.app.NotificationManager
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


inline val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

inline val Context.inputMethodManger: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
