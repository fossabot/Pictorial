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

    fun isShowing(): Boolean {
        return bottomDialogFragment.dialog?.isShowing ?: false
    }

    fun show(fm: FragmentManager, tag: String? = null) {
        bottomDialogFragment.show(fm, tag)
    }

    fun dismiss() {
        bottomDialogFragment.dismiss()
    }

    @SuppressLint("ValidFragment")
    private class BottomDialogFragment : DialogFragment() {
        private var customView: View? = null
        private var positiveText: String? = null
        private var negativeText: String? = null
        private lateinit var positiveClick: (View) -> Unit
        private lateinit var negativeClick: (View) -> Unit

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomMenu)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val bottomMenuView = inflate(R.layout.bottom_menu, container, false)
            if (customView != null)
                bottomMenuView.findViewById<ViewGroup>(R.id.container).addView(customView)
            val positiveButton = bottomMenuView.findViewById<Button>(R.id.positiveButton)
            positiveButton.setOnClickListener(negativeClick)
            positiveButton.text = positiveText ?: string(R.string.positive_button_text)
            val negativeButton = bottomMenuView.findViewById<Button>(R.id.negativeButton)
            negativeButton.setOnClickListener(negativeClick)
            negativeButton.text = negativeText ?: string(R.string.negative_button_text)
            return bottomMenuView
        }

        override fun onStart() {
            super.onStart()
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            customView = null
        }

        fun setCustomView(view: View) {
            customView = view
        }

        fun setPositiveButton(onClick: (View) -> Unit) {
            positiveClick = onClick
        }

        fun setPositiveText(text: String?) {
            positiveText = text
        }

        fun setNegativeButton(onClick: (View) -> Unit) {
            negativeClick = onClick
        }

        fun setNegativeText(text: String?) {
            negativeText = text
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class Builder {
        private val bottomMenu = BottomMenu()
        private var positiveText: String? = null
        private var negativeText: String? = null
        private var positiveClick: ((View) -> Unit)? = null
        private var negativeClick: ((View) -> Unit)? = null
        fun setCustomView(view: View): Builder {
            bottomMenu.bottomDialogFragment.setCustomView(view)
            return this
        }

        fun setPositiveButton(text: String, onClick: (View) -> Unit): Builder {
            negativeText = text
            negativeClick = onClick
            return this
        }

        fun setPositiveButton(onClick: (View) -> Unit): Builder {
            positiveClick = onClick
            return this
        }

        fun setPositiveText(text: String): Builder {
            positiveText = text
            return this
        }


        fun setNegativeButton(text: String, onClick: (View) -> Unit): Builder {
            negativeText = text
            negativeClick = onClick
            return this
        }

        fun setNegativeButton(onClick: (View) -> Unit): Builder {
            negativeClick = onClick
            return this
        }

        fun setNegativeText(text: String): Builder {
            negativeText = text
            return this
        }

        fun build(): BottomMenu {
            if (positiveClick == null)
                positiveClick = { bottomMenu.dismiss() }
            bottomMenu.bottomDialogFragment.setPositiveButton(positiveClick!!)
            bottomMenu.bottomDialogFragment.setPositiveText(positiveText)
            /*-----------------------------------------------------------------*/
            if (negativeClick == null)
                negativeClick = { bottomMenu.dismiss() }
            bottomMenu.bottomDialogFragment.setNegativeButton(negativeClick!!)
            bottomMenu.bottomDialogFragment.setNegativeText(negativeText)
            return bottomMenu
        }
    }
}