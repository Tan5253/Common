package com.like.common.sample.dialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.like.common.sample.R
import com.like.common.sample.databinding.DialogFragmentOneButtonNoTitleBinding

class OneButtonNoTitleDialog : DialogFragment() {

    companion object {
        val KEY_MESSAGE = "key_message"
        val KEY_BUTTON_NAME = "key_button_name"

        @JvmStatic fun newInstance(): OneButtonNoTitleDialog {
            return OneButtonNoTitleDialog()
        }

        @JvmStatic fun newInstance(args: Bundle): OneButtonNoTitleDialog {
            val dialog = OneButtonNoTitleDialog()
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DialogFragmentOneButtonNoTitleBinding>(inflater, R.layout.dialog_fragment_one_button_no_title, container, false) ?: return null
        binding.rlOuter.setOnClickListener {
            this@OneButtonNoTitleDialog.dismissAllowingStateLoss()
        }
        if (arguments != null) {
            val message = arguments.getString(KEY_MESSAGE)
            val buttonName = arguments.getString(KEY_BUTTON_NAME)

            binding.tvMessage.text = message
            binding.tvButton.text = buttonName

            binding.tvButton.setOnClickListener {
                this@OneButtonNoTitleDialog.dismissAllowingStateLoss()
            }
        }
        return binding.root
    }
}
