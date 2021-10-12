package com.collect.collectpeak.fragment.equipment.equipment_select

import android.os.Parcel
import android.os.Parcelable

class EquipmentData() : Parcelable{

    var name = ""

    var description = ""

    var isCheck = false

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        description = parcel.readString().toString()
        isCheck = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeByte(if (isCheck) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EquipmentData> {
        override fun createFromParcel(parcel: Parcel): EquipmentData {
            return EquipmentData(parcel)
        }

        override fun newArray(size: Int): Array<EquipmentData?> {
            return arrayOfNulls(size)
        }
    }

}