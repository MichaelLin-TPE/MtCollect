package com.collect.collectpeak.fragment.mountain.peak_preview

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

class SummitData() : Parcelable{

    var summitId = UUID.randomUUID().toString()

    var mtName = ""

    var mtLevel = ""

    var mtTime : Long = 0

    var photoArray = ArrayList<String>()

    constructor(parcel: Parcel) : this() {
        summitId = parcel.readString().toString()
        mtName = parcel.readString().toString()
        mtLevel = parcel.readString().toString()
        mtTime = parcel.readLong()
        parcel.createStringArrayList().let {
            if (it == null){
                return@let
            }
            photoArray = it
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(summitId)
        parcel.writeString(mtName)
        parcel.writeString(mtLevel)
        parcel.writeLong(mtTime)
        parcel.writeStringList(photoArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SummitData> {
        override fun createFromParcel(parcel: Parcel): SummitData {
            return SummitData(parcel)
        }

        override fun newArray(size: Int): Array<SummitData?> {
            return arrayOfNulls(size)
        }
    }


}