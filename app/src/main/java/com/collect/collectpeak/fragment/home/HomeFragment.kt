package com.collect.collectpeak.fragment.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentHomeBinding
import com.collect.collectpeak.dialog.LocationListDialog
import com.collect.collectpeak.fragment.home.news.NewsAdapter
import com.collect.collectpeak.fragment.home.weather.LocationData
import com.collect.collectpeak.log.MichaelLog
import java.util.ArrayList

class HomeFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding: FragmentHomeBinding

    private lateinit var adapter: HomeAdapter

    private val viewModel: HomeViewModel by activityViewModels {
        val repository = HomeRepositoryImpl()
        HomeViewModel.HomeViewModelFactory(repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        dataBinding.vm = viewModel
        val view = dataBinding.root
        dataBinding.lifecycleOwner = this
        initView()
        // Inflate the layout for this fragment
        return view
    }

    private fun initView() {
        MichaelLog.i("initHomeAdapter")
        adapter = HomeAdapter()

        adapter.setOnHomeListItemClickListener(object : HomeAdapter.OnHomeListItemClickListener {
            override fun onWeatherSpinnerClick(targetLocation: String) {
                viewModel.onWeatherSpinnerClick(targetLocation)
            }
        })

        dataBinding.homeRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        dataBinding.homeRecyclerView.adapter = adapter

        adapter.setOnNewsItemClickListener(object : NewsAdapter.OnNewsItemClickListener {
            override fun onNewsItemClick(url: String) {
                MichaelLog.i("點擊的網址：$url")
            }

        })


    }

    override fun onStart() {
        super.onStart()

        viewModel.onFragmentStart()

        observersHandle()

    }

    override fun onPause() {
        super.onPause()
        viewModel.locationListLiveData.value = null
        viewModel.locationListLiveData.removeObservers(this)
    }

    private fun observersHandle() {

        viewModel.weatherLiveData.observe(this, { weatherData ->
            MichaelLog.i("update HomeAdapter")
            adapter.setWeatherData(weatherData)
        })

        viewModel.locationListLiveData.observe(this, { locationList ->

            if (locationList == null) {
                return@observe
            }

            showLocationListDialog(locationList)


        })

        viewModel.newsListLiveData.observe(this, { newsList ->


            adapter.setNewsList(newsList)

        })

    }

    private fun showLocationListDialog(locationList: ArrayList<LocationData>) {

        MichaelLog.i("顯示地區列表")
        val locationDialog = LocationListDialog.newInstance()
        locationDialog.setLocationArray(locationList)
        locationDialog.show(fragmentActivity.supportFragmentManager, "dialog")

        locationDialog.setOnLocationConfirmButtonClickListener(object :
            LocationListDialog.OnLocationConfirmButtonClickListener {
            override fun onClick(location: String) {
                viewModel.onLocationConfirmButtonClickListener(location)
                locationDialog.dismiss()
            }
        })
    }

}