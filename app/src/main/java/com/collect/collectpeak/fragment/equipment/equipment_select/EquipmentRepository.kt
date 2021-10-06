package com.collect.collectpeak.fragment.equipment.equipment_select

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentListData

interface EquipmentRepository {


    fun getEquipmentList(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<EquipmentListData>)

}