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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.tool.DpConvertTool

class LoadingDialog : DialogFragment() {


    private lateinit var fragmentActivity: FragmentActivity

    private val handler = Handler(Looper.myLooper()!!)

    private lateinit var tvContent: TextView

    private var content = ""

    companion object {
        @JvmStatic
        fun newInstance(content: String) = LoadingDialog().apply {
            arguments = Bundle().apply {
                this.putString("content", content)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = fragmentActivity.layoutInflater
        val view = inflater.inflate(R.layout.loading_dialog_layout, null)
        val dialog = Dialog(fragmentActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        initView(view)
        dialog.setCancelable(false)
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wlp = window?.attributes
        wlp?.width = DpConvertTool.getDb(150)
        wlp?.height = DpConvertTool.getDb(150)
        window?.attributes = wlp
        return dialog
    }

    private fun initView(view: View) {
        isCancelable = false
        content = ""
        arguments?.apply {
            content = this.getString("content", "")
        }.toString()

        tvContent = view.findViewById(R.id.loading_content)
        handler.removeCallbacks(firstRunAnimation)
        handler.removeCallbacks(secondRunAnimation)
        handler.removeCallbacks(thirdRunAnimation)
        handler.removeCallbacks(finalRunAnimation)
        handler.postDelayed(firstRunAnimation, 500)
    }

    override fun dismiss() {
        super.dismiss()
        handler.removeCallbacks(firstRunAnimation)
        handler.removeCallbacks(secondRunAnimation)
        handler.removeCallbacks(thirdRunAnimation)
        handler.removeCallbacks(finalRunAnimation)
    }

    private val firstRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(secondRunAnimation, 500)
    }

    private val secondRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(thirdRunAnimation, 500)
    }

    private val thirdRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(finalRunAnimation, 500)
    }

    private val finalRunAnimation = Runnable {
        tvContent.text = content
        restartAnimation()
    }

    private fun restartAnimation() {
        handler.postDelayed(firstRunAnimation, 500)
    }

}