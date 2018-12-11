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

        override fun onDestroyView() {
            super.onDestroyView()
            customView = null
        }

        fun setCustomView(view: View) {
            customView = view
        }

        fun setPositiveButton(text: String?, onClick: (View) -> Unit) {
            val positiveButton = view?.findViewById<Button>(R.id.positiveButton) ?: return
            positiveButton.text = text ?: string(R.string.positive_button_text)
            positiveButton.setOnClickListener(onClick)
        }

        fun setPositiveText(text: String) {
            val positiveButton = view?.findViewById<Button>(R.id.positiveButton) ?: return
            positiveButton.text = text
        }

        fun setNegativeButton(text: String?, onClick: (View) -> Unit) {
            val negativeButton = view?.findViewById<Button>(R.id.positiveButton) ?: return
            negativeButton.text = text ?: string(R.string.negative_button_text)
            negativeButton.setOnClickListener(onClick)
        }

        fun setNegativeText(text: String) {
            val negativeButton = view?.findViewById<Button>(R.id.positiveButton) ?: return
            negativeButton.text = text
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class Builder {
        private val bottomMenu = BottomMenu()
        private var isSetPositiveButton = false
        private var isSetNegativeButton = false
        private var positiveText: String? = null
        private var negativeText: String? = null
        private var positiveClick: ((View) -> Unit)? = null
        private var negativeClick: ((View) -> Unit)? = null
        fun setCustomView(view: View): Builder {
            bottomMenu.bottomDialogFragment.setCustomView(view)
            return this
        }

        fun setPositiveButton(text: String?, onClick: (View) -> Unit): Builder {
            bottomMenu.bottomDialogFragment.setPositiveButton(text, onClick)
            isSetPositiveButton = true
            return this
        }

        fun setPositiveButton(onClick: (View) -> Unit): Builder {
            positiveClick = onClick
            isSetPositiveButton = true
            return this
        }

        fun setPositiveText(text: String): Builder {
            positiveText = text
            return this
        }


        fun setNegativeButton(text: String?, onClick: (View) -> Unit): Builder {
            bottomMenu.bottomDialogFragment.setNegativeButton(text, onClick)
            isSetNegativeButton = true
            return this
        }

        fun setNegativeButton(onClick: (View) -> Unit): Builder {
            negativeClick = onClick
            isSetNegativeButton = true
            return this
        }

        fun setNegativeText(text: String): Builder {
            negativeText = text
            return this
        }

        fun build(): BottomMenu {
            if (!isSetPositiveButton) {
                setPositiveButton {
                    if (bottomMenu.isShowing())
                        bottomMenu.dismiss()
                }
            }
            if (negativeText != null)
                bottomMenu.bottomDialogFragment.setNegativeText(negativeText!!)
            if (!isSetNegativeButton) {
                setNegativeButton {
                    if (bottomMenu.isShowing())
                        bottomMenu.dismiss()
                }
            }
            if (positiveText != null)
                bottomMenu.bottomDialogFragment.setPositiveText(positiveText!!)
            return bottomMenu
        }
    }
}