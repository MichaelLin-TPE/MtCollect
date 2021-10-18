package com.collect.collectpeak.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.custom_widget.CollectPeakEditTextView
import com.collect.collectpeak.tool.DpConvertTool

class EditContentDialog : DialogFragment() {


    private lateinit var fragmentActivity: FragmentActivity

    private var content = ""

    companion object {
        @JvmStatic
        fun newInstance(content: String) = EditContentDialog().apply {
            arguments = Bundle().apply {
                this.putString("content", content)
            }
        }
    }

    private lateinit var onEditContentDialogClickListener: OnEditContentDialogClickListener

    fun setOnEditContentDialogClickListener(onEditContentDialogClickListener: OnEditContentDialogClickListener) {
        this.onEditContentDialogClickListener = onEditContentDialogClickListener
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = fragmentActivity.layoutInflater
        val view = inflater.inflate(R.layout.edit_content_dialog_layout, null)
        val dialog = Dialog(fragmentActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        initView(view)
        dialog.setCancelable(false)
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wlp = window?.attributes
        wlp?.width = DpConvertTool.getDb(300)
        wlp?.height = DpConvertTool.getDb(150)
        window?.attributes = wlp
        return dialog
    }

    private fun initView(view: View) {

        arguments?.let {

            content = it.getString("content", "")

        }
        val editContent = view.findViewById<CollectPeakEditTextView>(R.id.edit_content)

        val btnConfirm = view.findViewById<TextView>(R.id.edit_dialog_confirm)

        val btnCancel = view.findViewById<TextView>(R.id.edit_dialog_cancel)

        editContent.hint = content

        btnConfirm.setOnClickListener {
            onEditContentDialogClickListener.onConfirm(editContent.text.toString())
            dismiss()
        }
        btnCancel.setOnClickListener {
            onEditContentDialogClickListener.onCancel()
            dismiss()
        }

    }

    interface OnEditContentDialogClickListener {
        fun onConfirm(content: String)

        fun onCancel()
    }

}