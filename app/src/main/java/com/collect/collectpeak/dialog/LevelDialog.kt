package com.collect.collectpeak.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import cn.carbswang.android.numberpickerview.library.NumberPickerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.custom_widget.CollectPeakButton
import com.collect.collectpeak.fragment.home.HomeFragment
import com.collect.collectpeak.fragment.home.weather.LocationData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.DpConvertTool
import com.collect.collectpeak.tool.TypeFaceHelper

class LevelDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = LevelDialog().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private lateinit var onLevelButtonClickListener: OnLevelButtonClickListener

    private var value = "全部"

    private var index = 0;

    fun setOnLevelButtonClickListener(onLevelButtonClickListener: OnLevelButtonClickListener) {
        this.onLevelButtonClickListener = onLevelButtonClickListener
    }


    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var levelArray: ArrayList<String>

    fun setLevelArray(levelArray: ArrayList<String>) {
        this.levelArray = levelArray
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = fragmentActivity.layoutInflater
        val view = inflater.inflate(R.layout.location_list_dialog_layout, null)
        val dialog = Dialog(fragmentActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        initView(view)
        dialog.setCancelable(false)
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wlp = window?.attributes
        wlp?.width = DpConvertTool.getDb(300)
        wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = wlp
        return dialog
    }

    private fun initView(view: View) {
        val levelPicker = view.findViewById<NumberPickerView>(R.id.location_picker)


        val levelList = arrayOfNulls<String>(levelArray.size)

        for ((index, level) in levelArray.withIndex()) {
            levelList[index] = level
        }
        levelPicker.displayedValues = levelList
        levelPicker.maxValue = levelArray.size - 1
        levelPicker.minValue = 0
        levelPicker.wrapSelectorWheel = true
        levelPicker.value = index
        levelPicker.setContentTextTypeface(
            TypeFaceHelper.get(
                MtCollectorApplication.getInstance().getContext(), "Jason.ttf"
            )
        )

        levelPicker.setOnValueChangeListenerInScrolling { _, _, newVal ->
            MichaelLog.i("選擇了 $newVal")
            value = levelArray[newVal]
        }

        val button = view.findViewById<CollectPeakButton>(R.id.location_btn)

        button.setOnClickListener {
            onLevelButtonClickListener.onClick(value)
        }

    }

    interface OnLevelButtonClickListener {
        fun onClick(level: String)
    }


}