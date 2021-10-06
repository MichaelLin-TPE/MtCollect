package com.collect.collectpeak.fragment.equipment.equipment_select

import androidx.lifecycle.MutableLiveData

class EquipmentItemViewModel(
    equipmentData: EquipmentData
) {

    val titleLiveData = MutableLiveData(equipmentData.name)
    val descriptionLiveData = MutableLiveData(equipmentData.description)
    val checkBoxLiveData = MutableLiveData(equipmentData.isCheck)

    interface OnEquipmentCheckClickListener{
        fun onCheck(data: EquipmentData)
    }

}