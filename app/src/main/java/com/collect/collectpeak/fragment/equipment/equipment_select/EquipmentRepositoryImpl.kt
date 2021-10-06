package com.collect.collectpeak.fragment.equipment.equipment_select

import com.collect.collectpeak.firebase.FireStoreHandler

class EquipmentRepositoryImpl : EquipmentRepository {

    override fun getEquipmentList(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<EquipmentListData>) {
        FireStoreHandler.getInstance().getEquipmentList(onFireStoreCatchDataListener)
    }


}