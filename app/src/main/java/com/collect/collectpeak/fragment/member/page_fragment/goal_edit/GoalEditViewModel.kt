package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GoalEditViewModel : ViewModel() {


    val mtNameLiveData = MutableLiveData<String>()

    val mtLevelLiveData = MutableLiveData<String>()

    val mtTimeLiveData = MutableLiveData<String>()

    val mtDescLiveData = MutableLiveData<String>()

    val mtPhotoListLivData = MutableLiveData<ArrayList<String>>()

    fun onFragmentStart(targetSummitData: SummitData) {

        mtNameLiveData.value = targetSummitData.mtName

        mtLevelLiveData.value = targetSummitData.mtLevel

        mtTimeLiveData.value = "時間 : ${SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(targetSummitData.mtTime))}"

        mtDescLiveData.value = targetSummitData.description

        mtPhotoListLivData.value = targetSummitData.photoArray

    }


    open class GoalEditFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalEditViewModel() as T
        }
    }
}