package com.collect.collectpeak.fragment.equipment.equipment_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.TempDataHandler

class EquipmentEditViewModel(private val repository: EquipmentEditRepository) : ViewModel() {

    val equipmentNameLiveData = MutableLiveData<String>()

    val equipmentListLiveData = MutableLiveData<ArrayList<EquipmentData>>()

    val showEditNameDialogLiveData = MutableLiveData<Boolean>()

    val goToEditEquipmentListPageLiveData = MutableLiveData<Boolean>()

    val showToastLiveData = MutableLiveData<String>()

    val finishPageLiveData = MutableLiveData<Boolean>()

    val showProgressLiveData = MutableLiveData<Boolean>()

    val dismissProgressDialog = MutableLiveData<Boolean>()

    private lateinit var targetUserData : EquipmentUserData

    fun onFragmentResume(userEquipmentData: EquipmentUserData) {

        if (TempDataHandler.getUserEquipmentList().isNotEmpty()){
            val dataArray = ArrayList<EquipmentData>(TempDataHandler.getUserEquipmentList())
            MichaelLog.i("上一頁有資料數量：${dataArray.size}")
            userEquipmentData.selectTargetArray = dataArray

            TempDataHandler.getUserEquipmentList().clear()
        }


        targetUserData = userEquipmentData

        equipmentNameLiveData.value = userEquipmentData.name

        equipmentListLiveData.value = userEquipmentData.selectTargetArray

    }

    fun editNameButtonClickListener() {

        showEditNameDialogLiveData.value = true

    }

    fun onCatchEquipmentListName(content: String) {
        targetUserData.name = content
        equipmentNameLiveData.value = content
    }

    fun editEquipmentListClickListener() {

        TempDataHandler.setUserEquipmentList(targetUserData.selectTargetArray)
        goToEditEquipmentListPageLiveData.value = true

    }

    fun onButtonDoneClickListener() {
        showProgressLiveData.value = true
        repository.saveUserEquipmentData(targetUserData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                finishPageLiveData.value = true
                dismissProgressDialog.value = true
            }

            override fun onCatchDataFail() {
                showToastLiveData.value = "不明錯誤，請稍後再試。"
                dismissProgressDialog.value = true
            }

        })
    }


    open class EquipmentEditFactory(private val repository: EquipmentEditRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentEditViewModel(repository) as T
        }
    }

}