package com.collect.collectpeak.fragment.home.weather

import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.api.json_object.WeatherObject
import com.collect.collectpeak.databinding.WeatherItemLayoutBinding
import com.collect.collectpeak.fragment.home.HomeAdapter

class WeatherViewHolder(private val dataBinding: WeatherItemLayoutBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    private lateinit var onHomeListItemClickListener: HomeAdapter.OnHomeListItemClickListener

    fun showView(weatherData: WeatherObject , targetLocation:String) {

        val viewModel = WeatherViewModel(dataBinding)

        viewModel.setTargetLocation(targetLocation)

        viewModel.setWeatherData(weatherData)

        viewModel.setOnHomeListItemClickListener(onHomeListItemClickListener)

        dataBinding.vm = viewModel

        dataBinding.executePendingBindings()


    }

    fun setOnHomeListItemClickListener(onHomeListItemClickListener: HomeAdapter.OnHomeListItemClickListener) {
        this.onHomeListItemClickListener = onHomeListItemClickListener
    }


}