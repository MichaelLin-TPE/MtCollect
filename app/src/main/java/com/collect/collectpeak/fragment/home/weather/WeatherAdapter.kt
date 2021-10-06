package com.collect.collectpeak.fragment.home.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.api.json_object.WeatherElement
import com.collect.collectpeak.databinding.WeatherWeekItemLayoutBinding
import com.collect.collectpeak.log.MichaelLog

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var weatherArray = ArrayList<WeatherElement>()

    private lateinit var rainArray: ArrayList<String>

    private lateinit var timeArray: ArrayList<String>

    private lateinit var tempArray: ArrayList<String>

    private lateinit var descriptionArray: ArrayList<String>

    fun setWeatherArray(weatherArray: ArrayList<WeatherElement>) {
        this.weatherArray = weatherArray

        rainArray = ArrayList()
        timeArray = ArrayList()
        tempArray = ArrayList()
        descriptionArray = ArrayList()


        for (element in weatherArray) {
            for ((timeIndex, time) in element.time.withIndex()) {
                if (timeIndex % 2 != 0){
                    continue
                }

                if (element.description.equals("12小時降雨機率")) {

                    rainArray.add(time.elementValue[0].value+"%")
                    timeArray.add(time.startTime.substring(0,10))
                    continue
                }

                if (element.description.equals("最高溫度")){
                    tempArray.add(time.elementValue[0].value+"℃")
                    continue
                }

                if (element.description.equals("天氣預報綜合描述")){

                    descriptionArray.add(time.elementValue[0].value)
                    continue

                }
            }
        }
        MichaelLog.i("rain : ${rainArray.size} time : ${timeArray.size}")

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding =
            WeatherWeekItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        MichaelLog.i("rain ${rainArray[position]}")

        val rain = rainArray[position]

        val temp = tempArray[position]

        val time = timeArray[position]

        val description = descriptionArray[position]

        holder.showView(rain, temp, time, description)

    }

    override fun getItemCount(): Int = rainArray.size


    class ViewHolder(private val dataBinding: WeatherWeekItemLayoutBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {


        fun showView(rain: String, temp: String, time: String, description: String) {

            val viewModel = WeekWeatherViewModel(dataBinding)

            viewModel.showView(rain, temp, time, description)

            dataBinding.vm = viewModel
            dataBinding.executePendingBindings()

        }


    }

}