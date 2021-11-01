package com.collect.collectpeak.fragment.member.page_fragment.goal
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData

class GoalViewModel : ViewModel() {

    val summitArrayLiveData = MutableLiveData<ArrayList<SummitData>>()

    val showEmptyViewLiveData = MutableLiveData(true)

    fun onFragmentResume() {
        FireStoreHandler.getInstance().getUserSummitData(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<SummitData>>{
            override fun onCatchDataSuccess(data: ArrayList<SummitData>) {

                if (data.isEmpty()){
                    showEmptyViewLiveData.value = true
                    return
                }
                showEmptyViewLiveData.value = false
                summitArrayLiveData.value = data


            }

            override fun onCatchDataFail() {
                showEmptyViewLiveData.value = true

            }

        })
    }


    open class GoalFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalViewModel() as T
        }
    }

}