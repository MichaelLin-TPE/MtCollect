package com.collect.collectpeak.fragment.equipment.equipment_edit

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

interface EquipmentEditRepository {
    fun saveUserEquipmentData(targetUserData: EquipmentUserData, onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<Unit>)
}