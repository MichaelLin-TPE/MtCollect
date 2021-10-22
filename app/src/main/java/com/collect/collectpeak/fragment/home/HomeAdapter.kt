package com.collect.collectpeak.fragment.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.api.json_object.WeatherObject
import com.collect.collectpeak.databinding.MtNewsLayoutBinding
import com.collect.collectpeak.databinding.WeatherItemLayoutBinding
import com.collect.collectpeak.fragment.home.news.NewsAdapter
import com.collect.collectpeak.fragment.home.news.NewsData
import com.collect.collectpeak.fragment.home.news.NewsViewHolder
import com.collect.collectpeak.fragment.home.peak_list.PeakListViewHolder
import com.collect.collectpeak.fragment.home.weather.WeatherViewHolder

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var weatherData = WeatherObject()

    private var newsList = ArrayList<NewsData>()

    private var targetLocation = ""

    private lateinit var onHomeListItemClickListener: OnHomeListItemClickListener

    private lateinit var onNewsItemClickListener: NewsAdapter.OnNewsItemClickListener

    fun setOnNewsItemClickListener(onNewsItemClickListener: NewsAdapter.OnNewsItemClickListener){
        this.onNewsItemClickListener = onNewsItemClickListener
    }

    fun setOnHomeListItemClickListener(onHomeListItemClickListener: OnHomeListItemClickListener) {
        this.onHomeListItemClickListener = onHomeListItemClickListener
    }


    fun setWeatherData(weatherObject: WeatherObject) {
        this.weatherData = weatherObject
        this.targetLocation = weatherObject.targetLocation
        notifyDataSetChanged()
    }

    fun setNewsList(newsList: ArrayList<NewsData>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    companion object {
        const val PEAK_LIST = 0
        const val WEATHER = 1
        const val NEWS = 2
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> PEAK_LIST
            1 -> WEATHER
            else -> NEWS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            PEAK_LIST -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.billboard_item_layout, parent, false)
                return PeakListViewHolder(view)
            }

            WEATHER -> {
                val dataBinding = WeatherItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return WeatherViewHolder(dataBinding)
            }
            else ->{
                val dataBinding = MtNewsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return NewsViewHolder(dataBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PeakListViewHolder) {
            holder.showView()
        }
        if (holder is WeatherViewHolder) {
            holder.setOnHomeListItemClickListener(onHomeListItemClickListener)
            holder.showView(weatherData,targetLocation)
        }
        if (holder is NewsViewHolder){
            holder.showView(newsList)
            holder.setOnNewsItemClickListener(onNewsItemClickListener)
        }
    }

    override fun getItemCount(): Int = 3



    interface OnHomeListItemClickListener {
        fun onWeatherSpinnerClick(targetLocation:String)
    }

}