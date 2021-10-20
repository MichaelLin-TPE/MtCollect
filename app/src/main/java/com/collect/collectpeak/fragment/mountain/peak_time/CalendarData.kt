package com.collect.collectpeak.fragment.mountain.peak_time

import android.os.Parcel
import android.os.Parcelable

class CalendarData() : Parcelable {

    var timeStamp: Long = 0

    var date: String = ""

    var isCheck: Boolean = false

    var month: String = ""

    var year: String = ""

    var targetDate = "$year/$month/$date"

    constructor(parcel: Parcel) : this() {
        timeStamp = parcel.readLong()
        date = parcel.readString().toString()
        isCheck = parcel.readByte() != 0.toByte()
        month = parcel.readString().toString()
        year = parcel.readString().toString()
        targetDate = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(timeStamp)
        parcel.writeString(date)
        parcel.writeByte(if (isCheck) 1 else 0)
        parcel.writeString(month)
        parcel.writeString(year)
        parcel.writeString(targetDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarData> {
        override fun createFromParcel(parcel: Parcel): CalendarData {
            return CalendarData(parcel)
        }

        override fun newArray(size: Int): Array<CalendarData?> {
            return arrayOfNulls(size)
        }
    }

}