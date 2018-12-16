@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.messy.util

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar

inline var Fragment.backgroundAlpha: Float
    get() = activity!!.backgroundAlpha
    set(value) {
        activity!!.backgroundAlpha = value
    }

inline fun Fragment.toast(charSequence: CharSequence) {
    context!!.toast(charSequence)
}

inline fun Fragment.longToast(charSequence: CharSequence) {
    context!!.longToast(charSequence)
}

inline fun Fragment.color(@ColorRes id: Int): Int {
    return context!!.color(id)
}

inline fun Fragment.string(@StringRes id: Int, vararg args: Any?): String {
    return context!!.string(id, *args)
}

inline fun Fragment.inflate(res: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View {
    return context!!.inflate(res, parent, attachToRoot)
}

inline fun Fragment.lightStatusBar() {
    activity!!.lightStatusBar()
}

inline fun Fragment.darkStatusBar() {
    activity!!.darkStatusBar()
}

inline fun Fragment.backToHome() {
    activity!!.backToHome()
}

inline fun Fragment.exitToHome() {
    activity!!.exitToHome()
}

inline fun Fragment.setTranslateFlag() {
    activity!!.setTranslateFlag()
}

inline fun Fragment.navigateUp() {
    NavHostFragment.findNavController(this).navigateUp()
}

/*
* @IdRes int resId, @Nullable Bundle args, @Nullable NavOptions navOptions,
            @Nullable Navigator.Extras navigatorExtras
*/
inline fun Fragment.navigate(
    @IdRes resId: Int, args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    NavHostFragment.findNavController(this).navigate(resId, args, navOptions, navigatorExtras)
}

inline fun Fragment.setFullScreen() {
    activity!!.setFullScreen()
}

inline fun Fragment.restoreScreen() {
    activity!!.restoreScreen()
}

inline val Fragment.displayWidth: Int
    get() = context!!.displayWidth

inline val Fragment.displayHeight: Int
    get() = context!!.displayHeight

inline fun Fragment.snack(msg: CharSequence): Snackbar {
    val snack = Snackbar.make(view!!, msg, Snackbar.LENGTH_SHORT)
    snack.show()
    applySnackBar(snack)
    return snack
}

inline fun Fragment.longSnack(msg: CharSequence): Snackbar {
    val snack = Snackbar.make(view!!, msg, Snackbar.LENGTH_LONG)
    snack.show()
    applySnackBar(snack)
    return snack
}