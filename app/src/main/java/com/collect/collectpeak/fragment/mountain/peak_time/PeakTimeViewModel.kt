package com.collect.collectpeak.fragment.mountain.peak_time

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.CalendarHandler
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PeakTimeViewModel : ViewModel() {


    val calendarLiveData = MutableLiveData<ArrayList<CalendarObject>>()

    val showYeaListDialogLiveData = MutableLiveData<String>()

    val updateCalendarLiveData = MutableLiveData<ArrayList<CalendarObject>>()

    val enableNextButtonLiveData = MutableLiveData(false)

    val goToSelectPhotoPageLiveData = MutableLiveData<CalendarData>()

    val yearLiveData = MutableLiveData(
        SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(Date(System.currentTimeMillis()))
    )

    private var targetCalendarArray = ArrayList<CalendarObject>()

    private var userSelectCalendarData = CalendarData()

    fun onFragmentStart() {

        CalendarHandler.instance.getYearCalendar(getCurrentYear(),
            object : CalendarHandler.OnCreateCalendarListener {
                override fun onComplete(calendarArray: ArrayList<CalendarObject>) {
                    MichaelLog.i("成功取得日曆資料，長度：${calendarArray.size}")
                    targetCalendarArray = calendarArray
                    calendarLiveData.value = calendarArray
                }
            })

    }

    private fun getCurrentYear(): Int {
        return SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(Date(System.currentTimeMillis())).toInt()
    }

    fun onPeakYearSpinnerClickListener() {
        showYeaListDialogLiveData.value = yearLiveData.value
    }

    fun onYearConfirmClickListener(year: String) {
        yearLiveData.value = year
        CalendarHandler.instance.getYearCalendar(year.toInt(),
            object : CalendarHandler.OnCreateCalendarListener {
                override fun onComplete(calendarArray: ArrayList<CalendarObject>) {
                    targetCalendarArray = calendarArray
                    updateCalendarLiveData.value = calendarArray

                    userSelectCalendarData = CalendarData()

                    checkUserSelectData()

                }
            })
    }

    private fun checkUserSelectData() {
        if (userSelectCalendarData.date.isEmpty()){
            enableNextButtonLiveData.value = false
            return
        }

        enableNextButtonLiveData.value = true
    }

    fun onCalendarDateClickListener(data: CalendarData) {

        targetCalendarArray.forEach {
            it.calendarArray.forEach { calendarData ->
                calendarData.isCheck = false
            }
        }

        userSelectCalendarData = data
        checkUserSelectData()
        targetCalendarArray.forEach {
            it.calendarArray.forEach { calendarData ->
                if (data.year == calendarData.year && data.month == calendarData.month && data.date == calendarData.date){
                    calendarData.isCheck = true
                    return@forEach
                }
            }
        }
        updateCalendarLiveData.value = targetCalendarArray

    }

    fun onNextButtonClickListener() {
        goToSelectPhotoPageLiveData.value = userSelectCalendarData
    }

    fun onPause() {
        enableNextButtonLiveData.value = false
    }


    open class PeakTimeViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PeakTimeViewModel() as T
        }

    }


}