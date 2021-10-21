package com.collect.collectpeak.fragment.mountain.peak_time

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData

class MtPeakData() : Parcelable{

    var mtName = ""

    var time : Long = 0

    var level = ""

    var photoArray = ArrayList<Bitmap>()

    var description = ""

    constructor(parcel: Parcel) : this() {
        mtName = parcel.readString().toString()
        time = parcel.readLong()
        level = parcel.readString().toString()
        description = parcel.readString().toString()
        parcel.createTypedArrayList(Bitmap.CREATOR).let {
            if (it == null){
                return@let
            }
            photoArray = it
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mtName)
        parcel.writeString(level)
        parcel.writeLong(time)
        parcel.writeString(description)
        parcel.writeTypedList(photoArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MtPeakData> {
        override fun createFromParcel(parcel: Parcel): MtPeakData {
            return MtPeakData(parcel)
        }

        override fun newArray(size: Int): Array<MtPeakData?> {
            return arrayOfNulls(size)
        }
    }


}