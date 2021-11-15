package com.collect.collectpeak.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.MessageActivity
import com.collect.collectpeak.activity.NoticeActivity
import com.collect.collectpeak.databinding.FragmentHomeBinding
import com.collect.collectpeak.dialog.LocationListDialog
import com.collect.collectpeak.fragment.home.news.NewsAdapter
import com.collect.collectpeak.fragment.home.weather.LocationData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.Tool
import java.util.ArrayList

class HomeFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding: FragmentHomeBinding

    private lateinit var adapter: HomeAdapter

    private var degree = 25f

    private val handler = Handler(Looper.myLooper()!!)

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
        dataBinding.clickListener = ButtonClickHandler(viewModel)
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

        viewModel.setOnHomeClickEventListener(object : OnHomeClickEventListener{
            override fun onStartNoticeAnimation() {
                doNoticeAnimation()
            }

            override fun onStopNoticeAnimation() {
                stopNoticeAnimation()
            }

            override fun onGotoNotificationPage() {
               val intent = Intent(fragmentActivity,NoticeActivity::class.java)
                fragmentActivity.startActivity(intent)
                Tool.startActivityInAnim(fragmentActivity,1)
            }

            override fun onGoToMessagePage() {
                val intent = Intent(fragmentActivity,MessageActivity::class.java)
                fragmentActivity.startActivity(intent)
                Tool.startActivityInAnim(fragmentActivity,1)
            }

        })
    }


    private fun stopNoticeAnimation() {
        handler.removeCallbacks(rotate45)
        handler.removeCallbacks(rotate45Again)
        dataBinding.homeActionBarNotice.clearAnimation()
        dataBinding.homeActionBarNotice.setImageResource(R.drawable.notice)
        dataBinding.homeActionBarNotice.rotation = 0f
    }

    private fun doNoticeAnimation() {
        handler.removeCallbacks(rotate45)
        handler.removeCallbacks(rotate45Again)
        dataBinding.homeActionBarNotice.clearAnimation()
        dataBinding.homeActionBarNotice.setImageResource(R.drawable.notice)
        dataBinding.homeActionBarNotice.rotation = 0f
        handler.post(rotate45)

    }

    private val rotate45 = Runnable {
        dataBinding.homeActionBarNotice.animate().rotationBy(degree).setDuration(200).start()
        degree = -60f
        doRotate45()

    }

    private fun doRotate45() {
        handler.postDelayed(rotate45Again,200)
    }

    private val rotate45Again = Runnable {
        dataBinding.homeActionBarNotice.animate().rotationBy(degree).setDuration(200).start()
        degree = 60f
        doRotate45Again()

    }

    private fun doRotate45Again() {
        handler.postDelayed(rotate45,200)
    }

    override fun onResume() {
        super.onResume()
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