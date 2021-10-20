package com.collect.collectpeak.fragment.mountain.peak_time

import android.os.Parcel
import android.os.Parcelable
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData

class CalendarObject() :Parcelable {

    var month :String = ""

    var year :String = ""

    var calendarArray :ArrayList<CalendarData> = ArrayList()

    constructor(parcel: Parcel) : this() {
        month = parcel.readString().toString()
        year = parcel.readString().toString()
        parcel.createTypedArrayList(CalendarData.CREATOR).let {
            if (it == null){
                return@let
            }
            calendarArray = it
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(month)
        parcel.writeString(year)
        parcel.writeTypedList(calendarArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarObject> {
        override fun createFromParcel(parcel: Parcel): CalendarObject {
            return CalendarObject(parcel)
        }

        override fun newArray(size: Int): Array<CalendarObject?> {
            return arrayOfNulls(size)
        }
    }


}