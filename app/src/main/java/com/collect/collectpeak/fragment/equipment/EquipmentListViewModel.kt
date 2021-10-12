package com.collect.collectpeak.fragment.equipment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.log.MichaelLog

class EquipmentListViewModel(private val equipmentListRepository: EquipmentListRepository) : ViewModel() {


    val defaultViewLiveData = MutableLiveData<Boolean>()

    val equipmentViewLiveData = MutableLiveData<Boolean>()

    val updateEquipmentListLiveData = MutableLiveData<ArrayList<EquipmentUserData>>()

    val goToEditPageLiveData = MutableLiveData<EquipmentUserData>()

    fun onFragmentStart() {

        defaultViewLiveData.value = true
        equipmentViewLiveData.value=  false
        equipmentListRepository.getCurrentUserEquipment(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>{
            override fun onCatchDataSuccess(data: ArrayList<EquipmentUserData>) {
                defaultViewLiveData.value = false
                equipmentViewLiveData.value =  true
                updateEquipmentListLiveData.value = data
            }

            override fun onCatchDataFail() {
                defaultViewLiveData.value = true
                equipmentViewLiveData.value = false
            }
        })




    }

    fun onEquipmentItemClickListener(data: EquipmentUserData) {
        MichaelLog.i("點擊：${data.name}")

        goToEditPageLiveData.value = data


    }


    open class EquipmentListViewModelFactory(private val equipmentListRepository: EquipmentListRepository) : ViewModelProvider.NewInstanceFactory(){

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentListViewModel(equipmentListRepository) as T
        }

    }

}