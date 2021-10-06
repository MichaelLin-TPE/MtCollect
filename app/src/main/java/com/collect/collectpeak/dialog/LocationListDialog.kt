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

class LocationListDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = LocationListDialog().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private lateinit var onLocationConfirmButtonClickListener: OnLocationConfirmButtonClickListener

    private var value = "臺北市"

    private var index = 9;

    fun setOnLocationConfirmButtonClickListener(onLocationConfirmButtonClickListener: OnLocationConfirmButtonClickListener) {
        this.onLocationConfirmButtonClickListener = onLocationConfirmButtonClickListener
    }


    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var locationArray: ArrayList<LocationData>

    fun setLocationArray(locationArray: ArrayList<LocationData>) {
        this.locationArray = locationArray
        for ((index, location) in locationArray.withIndex()) {
            value = if (location.isTarget) location.location else value
            this.index = if (location.isTarget) index else this.index
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


        val locationList = arrayOfNulls<String>(locationArray.size)

        for ((index, location) in locationArray.withIndex()) {
            MichaelLog.i("地區：$location")
            locationList[index] = location.location
        }
        locationPicker.displayedValues = locationList
        locationPicker.maxValue = locationArray.size - 1
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
            value = locationArray[newVal].location
        }

        val button = view.findViewById<CollectPeakButton>(R.id.location_btn)

        button.setOnClickListener {
            onLocationConfirmButtonClickListener.onClick(value)
        }

    }

    interface OnLocationConfirmButtonClickListener {
        fun onClick(location: String)
    }


}