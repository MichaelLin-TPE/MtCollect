package com.collect.collectpeak.fragment.equipment.equipment_edit

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

class EquipmentEditRepositoryImpl : EquipmentEditRepository{
    override fun saveUserEquipmentData(
        targetUserData: EquipmentUserData,
        onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<Unit>
    ) {
        FireStoreHandler.getInstance().saveSingleUserEquipmentData(targetUserData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                onFireStoreCatchDataListener.onCatchDataSuccess(data)
            }

            override fun onCatchDataFail() {
                onFireStoreCatchDataListener.onCatchDataFail()
            }

        })
    }
}