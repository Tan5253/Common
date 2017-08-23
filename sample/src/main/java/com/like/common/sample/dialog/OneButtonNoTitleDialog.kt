package com.like.common.sample.dialog

import android.os.Bundle
import com.like.common.sample.R
import com.like.common.sample.databinding.DialogFragmentOneButtonNoTitleBinding
import com.like.common.util.BaseDialogFragment

class OneButtonNoTitleDialog : BaseDialogFragment<DialogFragmentOneButtonNoTitleBinding>() {
    companion object {
        val KEY_MESSAGE = "key_message"
        val KEY_BUTTON_NAME = "key_button_name"
    }

    override fun initView(binding: DialogFragmentOneButtonNoTitleBinding, arguments: Bundle?) {
        binding.rlOuter.setOnClickListener {
            hide()
        }
        if (arguments != null) {
            val message = arguments.getString(OneButtonNoTitleDialog.KEY_MESSAGE)
            val buttonName = arguments.getString(OneButtonNoTitleDialog.KEY_BUTTON_NAME)

            binding.tvMessage.text = message
            binding.tvButton.text = buttonName

            binding.tvButton.setOnClickListener {
                hide()
            }
        }
    }

    override fun cancelable() = false

    override fun getDialogFragmentLayoutResId(): Int = R.layout.dialog_fragment_one_button_no_title

}
