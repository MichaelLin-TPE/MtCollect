package com.collect.collectpeak.fragment.equipment.equipment_select

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

class EquipmentUserData() : Parcelable{

    var name:String = ""

    var selectTargetArray = ArrayList<EquipmentData>()

    var equipmentID = UUID.randomUUID().toString()

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        equipmentID = parcel.readString().toString()
        parcel.createTypedArrayList(EquipmentData.CREATOR).let {
            if (it == null){
                return@let
            }
            selectTargetArray = it
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(equipmentID)
        parcel.writeTypedList(selectTargetArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EquipmentUserData> {
        override fun createFromParcel(parcel: Parcel): EquipmentUserData {
            return EquipmentUserData(parcel)
        }

        override fun newArray(size: Int): Array<EquipmentUserData?> {
            return arrayOfNulls(size)
        }
    }
}