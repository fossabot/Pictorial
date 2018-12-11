package com.messy.pictorial

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.messy.util.inflate
import com.messy.util.string

class BottomMenu {

    private val bottomDialogFragment = BottomDialogFragment()

    fun show(fm: FragmentManager, tag: String? = null) {
        bottomDialogFragment.show(fm, tag)
    }

    fun dismiss() {
        bottomDialogFragment.dismiss()
    }

    @SuppressLint("ValidFragment")
    private class BottomDialogFragment : DialogFragment() {
        private var customView: View? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomMenu)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val bottomMenuView = inflate(R.layout.bottom_menu, container, false)
            if (customView != null)
                bottomMenuView.findViewById<ViewGroup>(R.id.container).addView(customView)
            return bottomMenuView
        }

        override fun onStart() {
            super.onStart()
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }

        fun setCustomView(view: View) {
            customView = view
        }

        fun setPositiveButton(text: String?, onClick: (View) -> Unit) {
            val positiveButton = view?.findViewById<Button>(R.id.positiveButton) ?: return
            positiveButton.text = text ?: string(R.string.positive_button_text)
            positiveButton.setOnClickListener(onClick)
        }

        fun setNegativeButton(text: String?, onClick: (View) -> Unit) {
            val negativeButton = view?.findViewById<Button>(R.id.positiveButton) ?: return
            negativeButton.text = text ?: string(R.string.negative_button_text)
            negativeButton.setOnClickListener(onClick)
        }

    }

    class Builder() {
        private val bottomMenu = BottomMenu()
        fun setCustomView(view: View): Builder {
            bottomMenu.bottomDialogFragment.setCustomView(view)
            return this
        }

        fun setPositiveButton(text: String?, onClick: (View) -> Unit): Builder {
            bottomMenu.bottomDialogFragment.setPositiveButton(text, onClick)
            return this
        }

        fun setNegativeButton(text: String?, onClick: (View) -> Unit): Builder {
            bottomMenu.bottomDialogFragment.setNegativeButton(text, onClick)
            return this
        }

        fun build(): Builder {
            return this
        }
    }
}