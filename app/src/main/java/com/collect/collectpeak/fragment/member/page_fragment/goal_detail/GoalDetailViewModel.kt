package com.collect.collectpeak.fragment.member.page_fragment.goal_detail

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.StorageHandler
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.main_frame.OnBackButtonClickEventCallBackListener

class GoalDetailViewModel : ViewModel() {

    val showToastLiveData = MutableLiveData<String>()

    val showSummitList = MutableLiveData<ArrayList<SummitData>>()

    val scrollPositionLiveData = MutableLiveData(0)

    val updateSummitList = MutableLiveData<ArrayList<SummitData>>()

    val goToEditPageLiveData = MutableLiveData<SummitData>()

    val finishPageLiveData = MutableLiveData<Boolean>()

    private lateinit var onBackButtonClickEventCallBackListener: OnBackButtonClickEventCallBackListener

    fun setOnBackButtonClickEventCallBackListener(onBackButtonClickEventCallBackListener: OnBackButtonClickEventCallBackListener){
        this.onBackButtonClickEventCallBackListener = onBackButtonClickEventCallBackListener
    }

    private lateinit var allSummitList : ArrayList<SummitData>

    fun onFragmentStart(targetSummitData: SummitData, targetUid: String) {

        FireStoreHandler.getInstance().getUserSummitDataByUid(targetUid,object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<SummitData>>{
            override fun onCatchDataSuccess(data: ArrayList<SummitData>) {

                allSummitList = data

                showSummitList.value = data
                scrollToListPosition(data,targetSummitData)
            }

            override fun onCatchDataFail() {
               showToastLiveData.value = "不明錯誤,請稍後再試."
            }
        })
    }

    private fun scrollToListPosition(data: ArrayList<SummitData>, targetSummitData: SummitData) {

        var position = 0

        for ((index,summitData) in data.withIndex()){
            if (summitData.summitId == targetSummitData.summitId){
                position = index
                break
            }
        }

        scrollPositionLiveData.value = position

    }

    fun onSummitDataEditClickListener(data: SummitData) {

        MichaelLog.i("點擊 : ${data.mtTime}")
        goToEditPageLiveData.value = data


    }

    fun onSummitDataDeleteClickListener(data: SummitData) {

        startToRemovePhoto(data.photoArray)

        val iterator = allSummitList.iterator()

        while (iterator.hasNext()){

            val summitData = iterator.next()

            if (summitData.summitId == data.summitId){

                iterator.remove()
                break
            }
        }

        FireStoreHandler.getInstance().saveUserSummitList(allSummitList)

        updateSummitList.value = allSummitList

        if (allSummitList.isEmpty()){

            Handler(Looper.myLooper()!!).postDelayed({
                finishPageLiveData.value = true
            },500)

        }

    }



    private fun startToRemovePhoto(photoArray: java.util.ArrayList<String>) {

        StorageHandler.removePhoto(photoArray)

    }

    fun onBackButtonClickListener() {
        onBackButtonClickEventCallBackListener.onBackClick()
    }


    open class GoalDetailFactory : ViewModelProvider.NewInstanceFactory(){

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalDetailViewModel() as T
        }

    }

}