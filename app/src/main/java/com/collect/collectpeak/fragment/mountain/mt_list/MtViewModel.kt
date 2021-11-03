package com.collect.collectpeak.fragment.mountain.mt_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.google.gson.Gson
import kotlin.math.roundToInt

class MtViewModel(private val mtRepository: MtRepository) : ViewModel() {


    val mtListLiveData = MutableLiveData<ArrayList<MountainData>>()

    val mtSpinnerTitleLiveData = MutableLiveData("全部")

    val mtListUpdateLiveData = MutableLiveData<ArrayList<MountainData>>()

    val levelDialogLiveData = MutableLiveData<ArrayList<String>>()

    val mtGoalCountLiveData = MutableLiveData<String>("0%")
    
    private lateinit var targetMountainData: ArrayList<MountainData>

    fun onFragmentStart() {
        mtRepository.getMountainList(onFireStoreCatchDataListener)
        
    }

    fun onLevelSpinnerClickListener() {


        levelDialogLiveData.value = mtRepository.getLevelArray()


    }

    fun onLevelButtonClickListener(level: String) {

        mtSpinnerTitleLiveData.value = level

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

        if (level == "全部"){
            updateMtList.addAll(mtList)
        }

        mtListUpdateLiveData.value = updateMtList


    }

    private val onCatchSummitDataListener = object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<SummitData>>{
        override fun onCatchDataSuccess(data: ArrayList<SummitData>) {
            MichaelLog.i("取得登頂資料 : ${Gson().toJson(data)}" )
            targetMountainData.forEach{ mountainData ->  
                
                data.forEach {  summitData ->  
                    if (mountainData.name == summitData.mtName){
                        MichaelLog.i("有找到時間 : ${mountainData.name}")
                        mountainData.time = summitData.mtTime
                    }
                }
                
            }

            MichaelLog.i("登頂數量:${data.size} , 百岳總數 : ${targetMountainData.size}")

            val percent : String = (data.size * 100 / targetMountainData.size).toDouble()
                .roundToInt().toString() + "%"


            MichaelLog.i("percent : $percent")

            mtGoalCountLiveData.value = percent

            mtListLiveData.value = targetMountainData
            
        }

        override fun onCatchDataFail() {

        }

    }

    private val onFireStoreCatchDataListener = object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>{
        override fun onCatchDataSuccess(data: ArrayList<MountainData>) {
            targetMountainData = data
            
            mtListLiveData.value = data
            mtRepository.getUserSummitData(onCatchSummitDataListener)

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