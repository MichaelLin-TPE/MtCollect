package com.collect.collectpeak.main_frame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.main_frame.MainFrameActivity.Companion.GO_SELF_PAGE


class MainFrameViewModel(private val mainFrameRepository: MainFrameRepository) : ViewModel() {


    val tabDataLiveData = MutableLiveData<TabData>()

    val viewPagerLiveData = MutableLiveData<Boolean>()

    private lateinit var onNewIntentListener: OnNewIntentListener

    fun setOnNewIntentListener(onNewIntentListener: OnNewIntentListener){
        this.onNewIntentListener = onNewIntentListener
    }


    fun onActivityCreate() {
        //Tab 的資料
        val tabData = mainFrameRepository.getTabData()

        tabDataLiveData.value = tabData

        viewPagerLiveData.value = true

    }

    fun onActivityPause() {
        viewPagerLiveData.value = false
    }

    fun onCatchTypeByNewIntent(type: Int?) {
        if (type == GO_SELF_PAGE){
            onNewIntentListener.onGoToSelfPage()
        }
    }


    @Suppress("UNCHECKED_CAST")
    open class HomeViewModelFactory(private val mainFrameRepository: MainFrameRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            return MainFrameViewModel(mainFrameRepository) as T
        }
    }

    interface OnNewIntentListener{
        fun onGoToSelfPage()
    }

}