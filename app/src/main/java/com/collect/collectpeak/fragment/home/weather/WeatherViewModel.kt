package com.collect.collectpeak.fragment.home.weather

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.api.json_object.WeatherElement
import com.collect.collectpeak.api.json_object.WeatherObject
import com.collect.collectpeak.databinding.WeatherItemLayoutBinding
import com.collect.collectpeak.fragment.home.HomeAdapter
import com.collect.collectpeak.log.MichaelLog

class WeatherViewModel(private val dataBinding: WeatherItemLayoutBinding) {

    val locationNameLiveData = MutableLiveData("臺北市")

    private lateinit var locationArray : ArrayList<String>

    private var targetLocation = "臺北市"

    private lateinit var onHomeListItemClickListener: HomeAdapter.OnHomeListItemClickListener


    fun setTargetLocation(location:String){
        this.targetLocation = location
    }

    init {
        dataBinding.weatherSpinnerView.setOnClickListener {
            onHomeListItemClickListener.onWeatherSpinnerClick(targetLocation)
        }
    }


    fun setWeatherData(weatherData: WeatherObject) {

        if (weatherData.records == null){
            dataBinding.weatherItemRecyclerView.visibility = View.GONE
            dataBinding.weatherItemProgress.visibility = View.VISIBLE
            return
        }
        MichaelLog.i("顯示一週天氣")
        dataBinding.weatherItemRecyclerView.visibility = View.VISIBLE
        dataBinding.weatherItemProgress.visibility = View.GONE

        dataBinding.weatherItemRecyclerView.layoutManager = LinearLayoutManager(MtCollectorApplication.getInstance().getContext()).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }

        locationNameLiveData.value = targetLocation

        locationArray = ArrayList()

        for (location in weatherData.records.locations[0].location){
            locationArray.add(location.locationName)
        }

        val weatherElementArray = ArrayList<WeatherElement>()

        for (data in weatherData.records.locations){
            for (location in data.location){
                if (location.locationName.equals(targetLocation)){
                    weatherElementArray.addAll(location.weatherElement)
                    break
                }
            }
        }

        MichaelLog.i("weatherElementArray size : ${weatherElementArray.size} locationArray size : ${locationArray.size}")

        val adapter = WeatherAdapter()

        adapter.setWeatherArray(weatherElementArray)

        dataBinding.weatherItemRecyclerView.adapter = adapter

    }

    fun setOnHomeListItemClickListener(onHomeListItemClickListener: HomeAdapter.OnHomeListItemClickListener) {

        this.onHomeListItemClickListener = onHomeListItemClickListener

    }


}