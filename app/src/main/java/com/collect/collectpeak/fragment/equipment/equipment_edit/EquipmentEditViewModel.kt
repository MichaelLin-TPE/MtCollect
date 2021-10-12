package com.collect.collectpeak.fragment.equipment.equipment_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

class EquipmentEditViewModel : ViewModel() {

    val equipmentNameLiveData = MutableLiveData<String>()

    val equipmentListLiveData = MutableLiveData<ArrayList<EquipmentData>>()

    fun onFragmentResume(userEquipmentData: EquipmentUserData) {

        equipmentNameLiveData.value = userEquipmentData.name

        equipmentListLiveData.value = userEquipmentData.selectTargetArray

    }


    open class EquipmentEditFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentEditViewModel() as T
        }
    }

}