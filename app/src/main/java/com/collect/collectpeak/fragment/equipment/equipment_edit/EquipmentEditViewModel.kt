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

    private lateinit var targetUserData : EquipmentUserData

    private lateinit var onEquipmentEditClickEventListener: OnEquipmentEditClickEventListener

    fun  setOnEquipmentEditClickEventListener(onEquipmentEditClickEventListener: OnEquipmentEditClickEventListener){
        this.onEquipmentEditClickEventListener = onEquipmentEditClickEventListener
    }

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

        onEquipmentEditClickEventListener.onShowEditNameDialog()

    }

    fun onCatchEquipmentListName(content: String) {
        targetUserData.name = content
        equipmentNameLiveData.value = content
    }

    fun editEquipmentListClickListener() {

        TempDataHandler.setUserEquipmentList(targetUserData.selectTargetArray)
        onEquipmentEditClickEventListener.onGoToEditEquipmentListPage()

    }

    fun onButtonDoneClickListener() {
        onEquipmentEditClickEventListener.onShowProgress("處理中")
        repository.saveUserEquipmentData(targetUserData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                onEquipmentEditClickEventListener.onDismissProgressDialog()
                onEquipmentEditClickEventListener.onFinishPage()
            }

            override fun onCatchDataFail() {
                onEquipmentEditClickEventListener.onDismissProgressDialog()
                onEquipmentEditClickEventListener.onShowToast("發生不知名錯誤，請稍後再試。")
            }

        })
    }


    open class EquipmentEditFactory(private val repository: EquipmentEditRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentEditViewModel(repository) as T
        }
    }

}