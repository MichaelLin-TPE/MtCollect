package com.collect.collectpeak.fragment.home.weather

import androidx.lifecycle.MutableLiveData
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.WeatherWeekItemLayoutBinding

class WeekWeatherViewModel(private val dataBinding: WeatherWeekItemLayoutBinding) {


    val rainLiveData = MutableLiveData<String>()

    val tempLiveData = MutableLiveData<String>()

    val dateLiveData = MutableLiveData<String>()

    fun showView(rain: String, temp: String, time: String, description: String) {

        rainLiveData.value = rain

        tempLiveData.value = temp

        dateLiveData.value = time

        when {
            description.contains("雲") -> {
                dataBinding.weatherBigIcon.setImageResource(R.drawable.cloudy)
            }
            description.contains("晴") -> {
                dataBinding.weatherBigIcon.setImageResource(R.drawable.sun)
            }
            description.contains("雨") -> {
                dataBinding.weatherBigIcon.setImageResource(R.drawable.rain)
            }
            else -> {
                dataBinding.weatherBigIcon.setImageResource(R.drawable.cloudy)
            }
        }

    }
}