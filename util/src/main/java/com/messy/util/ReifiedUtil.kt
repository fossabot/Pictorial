package com.messy.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> Activity.startActivity(key: String, extra: Bundle) {
    startActivity(Intent(this, T::class.java).apply { putExtra(key, extra) })
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) {
    startActivityForResult(Intent(this, T::class.java), requestCode)
}

inline fun <reified T : Activity> Activity.startActivityForResult(key: String, extra: Bundle, requestCode: Int) {
    startActivityForResult(Intent(this, T::class.java).apply { putExtra(key, extra) }, requestCode)
}
/*--------------------------------------------------------------------------------------------------------------------*/

inline fun <reified T : Activity> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T : Activity> Fragment.startActivity(key: String, extra: Bundle) {
    startActivity(Intent(context, T::class.java).apply { putExtra(key, extra) })
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int) {
    startActivityForResult(Intent(context, T::class.java), requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(key: String, extra: Bundle, requestCode: Int) {
    startActivityForResult(Intent(context, T::class.java).apply { putExtra(key, extra) }, requestCode)
}

