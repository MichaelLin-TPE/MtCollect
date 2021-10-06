package com.collect.collectpeak.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.api.json_object.WeatherObject
import com.collect.collectpeak.fragment.home.news.NewsData
import com.collect.collectpeak.fragment.home.weather.LocationData
import com.collect.collectpeak.log.MichaelLog

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherObject>()

    val locationListLiveData = MutableLiveData<ArrayList<LocationData>>()

    val newsListLiveData = MutableLiveData<ArrayList<NewsData>>()

    private lateinit var weatherData : WeatherObject

    private var targetLocation = "臺北市"

    fun onFragmentStart() {

        homeRepository.getWeatherApi(object : HomeRepository.OnCatchWeekWeatherApiListener{
            override fun onCatchWeather(weatherObject: WeatherObject) {
                MichaelLog.i("成功取得天氣資料")
                weatherObject.targetLocation = "臺北市"
                weatherData = weatherObject
                weatherLiveData.value = weatherObject
            }
        })

        homeRepository.getNews(object : HomeRepository.OnCatchMtNewsListener{
            override fun onCatchNews(newsArray: ArrayList<NewsData>) {
                newsListLiveData.value = newsArray
            }

            override fun onCatchNewsFail() {

            }

        })

    }

    fun onWeatherSpinnerClick(targetLocation: String) {

        this.targetLocation = targetLocation
        val locationArray = homeRepository.getLocationArray().apply {
            this.forEach {
                it.isTarget = it.location == targetLocation
            }
        }

        if (locationArray.isEmpty()){
            return
        }

        locationListLiveData.value = locationArray

    }

    fun onLocationConfirmButtonClickListener(location: String) {

        this.targetLocation = location
        MichaelLog.i("選擇到的地區為：$location")

        weatherData.targetLocation = location

        weatherLiveData.value = weatherData



    }


    open class HomeViewModelFactory(private val homeRepository: HomeRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(homeRepository) as T
        }
    }

}