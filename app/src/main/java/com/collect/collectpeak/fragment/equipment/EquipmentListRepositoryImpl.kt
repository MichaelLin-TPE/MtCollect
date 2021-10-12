package com.collect.collectpeak.fragment.equipment

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

class EquipmentListRepositoryImpl : EquipmentListRepository {

    override fun getCurrentUserEquipment(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>) {
        FireStoreHandler.getInstance().getUserEquipmentData(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>{
            override fun onCatchDataSuccess(data: ArrayList<EquipmentUserData>) {
                onFireStoreCatchDataListener.onCatchDataSuccess(data)
            }

            override fun onCatchDataFail() {
                onFireStoreCatchDataListener.onCatchDataFail()
            }

        })
    }

}