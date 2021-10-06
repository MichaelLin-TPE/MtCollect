package com.collect.collectpeak.fragment.home

import com.collect.collectpeak.api.json_object.WeatherObject
import com.collect.collectpeak.fragment.home.news.NewsData
import com.collect.collectpeak.fragment.home.weather.LocationData

interface HomeRepository {
    
    fun getNews(onCatchMtNewsListener: OnCatchMtNewsListener)
    fun getWeatherApi(onCatchWeekWeatherApiListener: OnCatchWeekWeatherApiListener)
    fun getLocationArray(): ArrayList<LocationData>

    interface OnCatchMtNewsListener{
        fun onCatchNews(newsArray : ArrayList<NewsData>)
        fun onCatchNewsFail()
    }

    interface OnCatchWeekWeatherApiListener{
        fun onCatchWeather(weatherObject: WeatherObject)
    }

}