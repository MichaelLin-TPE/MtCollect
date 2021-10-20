package com.collect.collectpeak.tool

import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.mountain.peak_time.CalendarData
import com.collect.collectpeak.fragment.mountain.peak_time.CalendarObject
import com.collect.collectpeak.log.MichaelLog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarHandler {

    companion object{
        val instance = CalendarHandler()
    }

    private val weekArray = arrayOf(MtCollectorApplication.getInstance().getContext().getString(R.string.title_sun),
        MtCollectorApplication.getInstance().getContext().getString(R.string.title_mon),
        MtCollectorApplication.getInstance().getContext().getString(R.string.title_tue),
        MtCollectorApplication.getInstance().getContext().getString(R.string.title_wed),
        MtCollectorApplication.getInstance().getContext().getString(R.string.title_thur),
        MtCollectorApplication.getInstance().getContext().getString(R.string.title_fri),
        MtCollectorApplication.getInstance().getContext().getString(R.string.title_sat))
    
    fun getYearCalendar(year:Int , onCreateCalendarListener: OnCreateCalendarListener){

        Thread{

            val calendarArray = ArrayList<CalendarObject>()

            for (month in 1..12){

                val monthData = CalendarObject()

                monthData.month = month.toString()

                monthData.year = year.toString()

                val targetMonth = if (month < 10) "0$month" else month.toString()

                val monthEmptyIndex = getMonthEmptyIndex("$year/$targetMonth/01",weekArray)

                val monthMaxDate = getMaxMonthDate(year,month)

                MichaelLog.i("月份：$month 天數：$monthMaxDate")

                val monthDataArray = ArrayList<CalendarData>()

                for (date in 0..monthMaxDate + monthEmptyIndex){

                    setUpMonthDate(date,monthEmptyIndex,monthMaxDate,monthData,monthDataArray)

                }
                monthData.calendarArray = monthDataArray

                calendarArray.add(monthData)

            }

            val finalCalendarArray = ArrayList<CalendarObject>()

            val calendarObservable = Observable.fromIterable(calendarArray).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            calendarObservable.subscribe(object :Observer<CalendarObject>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: CalendarObject) {
                    finalCalendarArray.add(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {
                    onCreateCalendarListener.onComplete(finalCalendarArray)
                }

            })

        }.start()




    }

    private fun getMaxMonthDate(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, 1)
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)

    }

    private fun setUpMonthDate(
        date: Int,
        monthEmptyIndex: Int,
        monthMaxDate :Int,
        monthData: CalendarObject,
        monthDataArray: ArrayList<CalendarData>
    ) {
        if (date < monthEmptyIndex){
            val data = CalendarData()
            monthDataArray.add(data)
        }else if (date - monthEmptyIndex +1 <= monthMaxDate){
            val targetMonthDate = if (date - monthEmptyIndex +1 < 10) "0${date - monthEmptyIndex +1}" else (date - monthEmptyIndex + 1).toString()
            val targetMonth = if (monthData.month.toInt() < 10) "0${monthData.month}" else monthData.month
            val data = CalendarData()
            data.date = targetMonthDate
            data.month = targetMonth
            data.year = monthData.year
            data.timeStamp = getTimeStamp("${data.year}/$targetMonth/${data.date})")
            monthDataArray.add(data)
        }
    }

    private fun getTimeStamp(time: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        var date = Date()
        try {

            date = simpleDateFormat.parse(time)

        }catch (e : java.lang.Exception){
            e.printStackTrace()
        }

        return date.time
    }


    interface OnCreateCalendarListener{
        fun onComplete(calendarArray : ArrayList<CalendarObject>)
    }


    private fun getMonthEmptyIndex(currentDate: String, weekArray: Array<String>): Int {

        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val date: Date?
        var index = 0
        try {
            date = simpleDateFormat.parse(currentDate)

            val weekDay = getWeekDay(date.time)

            for (day in weekArray) {
                if (day == weekDay) {
                    break
                }
                index++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return index

    }

    private fun getWeekDay(time: Long): String = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(time))
}