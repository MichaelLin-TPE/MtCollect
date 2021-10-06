package com.collect.collectpeak.fragment.home

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.collect.collectpeak.api.ApiHandler
import com.collect.collectpeak.api.json_object.WeatherObject
import com.collect.collectpeak.fragment.home.news.NewsData
import com.collect.collectpeak.fragment.home.weather.LocationData
import com.collect.collectpeak.log.MichaelLog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import java.net.URL

@SuppressLint("CheckResult")
class HomeRepositoryImpl : HomeRepository {


    private val disposable = CompositeDisposable()

    private var weatherData = WeatherObject()

    override fun getNews(onCatchMtNewsListener: HomeRepository.OnCatchMtNewsListener) {

        Thread{
            try {
                startToCatchNewsData(onCatchMtNewsListener)
            } catch (e: Exception) {
                MichaelLog.i("取得新聞失敗：$e")
                onCatchMtNewsListener.onCatchNewsFail()
            }
        }.start()
    }

    private fun startToCatchNewsData(onCatchMtNewsListener: HomeRepository.OnCatchMtNewsListener){

        val dataArray = ArrayList<NewsData>()

        val url  = URL("https://npm.cpami.gov.tw/news_1.aspx?unit=c951cdcd-b75a-46b9-8002-8ef952ec95fd")

        val doc = Jsoup.parse(url, 3000)

        val title = doc.select("td")
        var data = NewsData()
        for (i in 0..29){
            val targetTitle = title[i].select("td")
            if (i == 0 || i == 3 || i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21 || i == 24 || i == 27) {
                data.location = targetTitle[0].text()
            } else if (i == 1 || i == 4 || i == 7 || i == 10 || i == 13 || i == 16 || i == 19 || i == 22 || i == 25 || i == 28) {
                data.time = targetTitle[0].text()
            } else {
                data.title = targetTitle[0].text()
                if (data.time.isNotEmpty() && data.location.isNotEmpty() && data.time.isNotEmpty()){
                    dataArray.add(data)
                    data = NewsData()
                }
            }
        }
        val news = doc.select("a[href]")
        var count = 0
        for ((index,newsData) in news.withIndex()){
            if (index in 27..36){
                val targetUrl = newsData.select("a[href]")
                dataArray[count].url= targetUrl.attr("abs:href")
                count++
            }
        }
        MichaelLog.i("成功取得新聞：${dataArray.size}")


        val finalNewsList = ArrayList<NewsData>()

        val newListObserver = Observable
            .fromIterable(dataArray)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        newListObserver.subscribe(object :Observer<NewsData>{
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onNext(t: NewsData) {
                finalNewsList.add(t)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
                onCatchMtNewsListener.onCatchNews(finalNewsList)
            }

        })





    }


    override fun getWeatherApi(onCatchWeekWeatherApiListener: HomeRepository.OnCatchWeekWeatherApiListener) {
        var weatherData = WeatherObject()

        ApiHandler.getRequestApi()
            .getWeatherApi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WeatherObject> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(data: WeatherObject) {
                    weatherData = data
                }

                override fun onError(e: Throwable) {
                    MichaelLog.i("取得天氣資料失敗：$e")
                }

                override fun onComplete() {
                    this@HomeRepositoryImpl.weatherData = weatherData
                    onCatchWeekWeatherApiListener.onCatchWeather(weatherData)
                }
            })


    }

    override fun getLocationArray(): ArrayList<LocationData> {

        val locationArray = ArrayList<LocationData>()

        if (weatherData.records == null){
            return locationArray
        }

        for (location in weatherData.records.locations[0].location){
            val locationData = LocationData()
            locationData.location = location.locationName
            locationData.isTarget = false
            locationArray.add(locationData)
        }

        return locationArray
    }
}