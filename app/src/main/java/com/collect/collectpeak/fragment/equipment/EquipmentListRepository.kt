package com.collect.collectpeak.fragment.equipment

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

interface EquipmentListRepository {
    fun getCurrentUserEquipment(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>)
}