package com.collect.collectpeak.tool

import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

class TempDataHandler {

    companion object{
        private var userEquipmentList  = ArrayList<EquipmentData>()


        fun setUserEquipmentList(userEquipmentList : ArrayList<EquipmentData>){
            this.userEquipmentList = userEquipmentList
        }

        fun getUserEquipmentList() : ArrayList<EquipmentData> = userEquipmentList

    }

}