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
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.home.HomeFragment
import com.collect.collectpeak.fragment.home.weather.LocationData
import com.collect.collectpeak.fragment.mountain.peak_time.YearData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.DpConvertTool
import com.collect.collectpeak.tool.TypeFaceHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PeakSelectDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = PeakSelectDialog().apply {
            arguments = Bundle().apply {

            }
        }
    }

   private lateinit var onPeakConfirmButtonClickListener: OnPeakConfirmButtonClickListener

    private var value = "臺北市"

    private var level = ""

    private var index = 9;

    private var mountainList = ArrayList<MountainData>()

    fun setOnPeakConfirmButtonClickListener(onPeakConfirmButtonClickListener: OnPeakConfirmButtonClickListener){
        this.onPeakConfirmButtonClickListener = onPeakConfirmButtonClickListener
    }


    private lateinit var fragmentActivity: FragmentActivity

    fun setPeak(peak: String, mountainList: ArrayList<MountainData>) {

        this.mountainList = mountainList

        value = peak

        for((targetIndex,mt) in mountainList.withIndex()){
            if (mt.name == peak){
                index = targetIndex
                break
            }
        }

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

        val locationPicker = view.findViewById<NumberPickerView>(R.id.location_picker)

        val mtList = arrayOfNulls<String>(mountainList.size)

        for ((index, mt) in mountainList.withIndex()) {
            mtList[index] = mt.name
        }

        locationPicker.displayedValues = mtList
        locationPicker.maxValue = mtList.size - 1
        locationPicker.minValue = 0
        locationPicker.wrapSelectorWheel = true
        locationPicker.value = index
        locationPicker.setContentTextTypeface(
            TypeFaceHelper.get(
                MtCollectorApplication.getInstance().getContext(), "Jason.ttf"
            )
        )

        locationPicker.setOnValueChangeListenerInScrolling { _, _, newVal ->
            value = mtList[newVal].toString()
            level = mountainList[newVal].difficulty
            MichaelLog.i("選擇了 $value")
        }

        val button = view.findViewById<CollectPeakButton>(R.id.location_btn)

        button.setOnClickListener {
            onPeakConfirmButtonClickListener.onClick(value,level)
            dismiss()
        }

    }


    interface OnPeakConfirmButtonClickListener {
        fun onClick(peak: String,level :String)
    }


}