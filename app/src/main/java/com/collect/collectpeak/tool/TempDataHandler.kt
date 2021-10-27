package com.collect.collectpeak.tool

import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

class TempDataHandler {

    companion object{
        private var userEquipmentList  = ArrayList<EquipmentData>()

        private var userSelectTimeStamp : Long = 0

        fun setUserEquipmentList(userEquipmentList : ArrayList<EquipmentData>){
            this.userEquipmentList = userEquipmentList
        }

        fun getUserEquipmentList() : ArrayList<EquipmentData> = userEquipmentList

        fun setUserSelectDate(timeStamp : Long){
            userSelectTimeStamp = timeStamp
        }

        fun getUserSelectTimeStamp() : Long = userSelectTimeStamp

    }

}