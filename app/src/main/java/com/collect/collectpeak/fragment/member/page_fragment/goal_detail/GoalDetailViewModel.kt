package com.collect.collectpeak.fragment.member.page_fragment.goal_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.StorageHandler
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog

class GoalDetailViewModel : ViewModel() {

    val showToastLiveData = MutableLiveData<String>()

    val showSummitList = MutableLiveData<ArrayList<SummitData>>()

    val scrollPositionLiveData = MutableLiveData(0)

    val updateSummitList = MutableLiveData<ArrayList<SummitData>>()

    val goToEditPageLiveData = MutableLiveData<SummitData>()

    private lateinit var allSummitList : ArrayList<SummitData>

    fun onFragmentStart(targetSummitData: SummitData) {
        FireStoreHandler.getInstance().getUserSummitData(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<SummitData>>{
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


    }

    private fun startToRemovePhoto(photoArray: java.util.ArrayList<String>) {

        StorageHandler.removePhoto(photoArray)

    }


    open class GoalDetailFactory : ViewModelProvider.NewInstanceFactory(){

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalDetailViewModel() as T
        }

    }

}