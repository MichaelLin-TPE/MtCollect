package com.collect.collectpeak.fragment.mountain.mt_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData

class MtViewModel(private val mtRepository: MtRepository) : ViewModel() {


    val mtListLiveData = MutableLiveData<ArrayList<MountainData>>()

    val mtSpinnerTitleLiveData = MutableLiveData("全部")

    val mtListUpdateLiveData = MutableLiveData<ArrayList<MountainData>>()

    val levelDialogLiveData = MutableLiveData<ArrayList<String>>()

    fun onFragmentStart() {
        mtRepository.getMountainList(onFireStoreCatchDataListener)
    }

    fun onLevelSpinnerClickListener() {


        levelDialogLiveData.value = mtRepository.getLevelArray()


    }

    fun onLevelButtonClickListener(level: String) {

        val mtList = mtListLiveData.value as ArrayList<MountainData>

        if (mtList.isNullOrEmpty()){
            return
        }
        val updateMtList = ArrayList<MountainData>()
        for (data in mtList){
            if (data.difficulty == level){
                updateMtList.add(data)
            }
        }
        mtListUpdateLiveData.value = updateMtList


    }

    private val onFireStoreCatchDataListener = object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>{
        override fun onCatchDataSuccess(data: ArrayList<MountainData>) {

            mtListLiveData.value = data

        }

        override fun onCatchDataFail() {



        }

    }





    open class MtViewHolderFactory(private val mtRepository: MtRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MtViewModel(mtRepository) as T
        }
    }

}