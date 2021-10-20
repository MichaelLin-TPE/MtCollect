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
import com.collect.collectpeak.fragment.mountain.peak_time.YearData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.DpConvertTool
import com.collect.collectpeak.tool.TypeFaceHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class YearSelectDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = YearSelectDialog().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private lateinit var onYearConfirmButtonClickListener: OnYearConfirmButtonClickListener

    private var value = "臺北市"

    private var index = 9;

    fun setOnYearConfirmButtonClickListener(onYearConfirmButtonClickListener: OnYearConfirmButtonClickListener) {
        this.onYearConfirmButtonClickListener = onYearConfirmButtonClickListener
    }


    private lateinit var fragmentActivity: FragmentActivity

    private var yearArray : ArrayList<YearData> = ArrayList()


    fun setYear(targetYear: String) {
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(System.currentTimeMillis())).toInt()
        value = targetYear
        for ((targetIndex, year) in (1955..currentYear).withIndex()){
            val data = YearData()
            data.year = year.toString()

            yearArray.add(data)
            if (data.year == targetYear){
                index = targetIndex
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

        val yearList = arrayOfNulls<String>(yearArray.size)

        for ((index, year) in yearArray.withIndex()) {
            MichaelLog.i("年份：$year")
            yearList[index] = year.year
        }

        locationPicker.displayedValues = yearList
        locationPicker.maxValue = yearList.size - 1
        locationPicker.minValue = 0
        locationPicker.wrapSelectorWheel = true
        locationPicker.value = index
        locationPicker.setContentTextTypeface(
            TypeFaceHelper.get(
                MtCollectorApplication.getInstance().getContext(), "Jason.ttf"
            )
        )

        locationPicker.setOnValueChangeListenerInScrolling { _, _, newVal ->
            MichaelLog.i("選擇了 $newVal")
            value = yearArray[newVal].year
        }

        val button = view.findViewById<CollectPeakButton>(R.id.location_btn)

        button.setOnClickListener {
            onYearConfirmButtonClickListener.onClick(value)
            dismiss()
        }

    }



    interface OnYearConfirmButtonClickListener {
        fun onClick(year: String)
    }


}