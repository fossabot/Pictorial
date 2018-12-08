package com.messy.util

import android.app.Activity
import androidx.fragment.app.Fragment

fun Activity.showTip(msg: CharSequence): Tip {
    val r = Tip(this, msg)
    r.show()
    return r
}

fun Fragment.showTip(msg: CharSequence): Tip {
    val r = Tip(activity!!, msg)
    r.show()
    return r
}
