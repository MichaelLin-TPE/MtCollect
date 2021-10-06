package com.collect.collectpeak.main_frame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainFrameViewModel(private val mainFrameRepository: MainFrameRepository) : ViewModel() {


    val tabDataLiveData = MutableLiveData<TabData>()

    val viewPagerLiveData = MutableLiveData<Boolean>()


    fun onActivityCreate() {
        //Tab 的資料
        val tabData = mainFrameRepository.getTabData()

        tabDataLiveData.value = tabData

        viewPagerLiveData.value = true

    }

    fun onActivityPause() {
        viewPagerLiveData.value = false
    }


    @Suppress("UNCHECKED_CAST")
    open class HomeViewModelFactory(private val mainFrameRepository: MainFrameRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            return MainFrameViewModel(mainFrameRepository) as T
        }
    }

}