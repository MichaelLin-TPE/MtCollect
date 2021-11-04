package com.collect.collectpeak.fragment.share

import android.os.Parcel
import android.os.Parcelable

class MessageData() : Parcelable{

    var message = ""

    var uid = ""

    constructor(parcel: Parcel) : this() {
        message = parcel.readString().toString()
        uid = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageData> {
        override fun createFromParcel(parcel: Parcel): MessageData {
            return MessageData(parcel)
        }

        override fun newArray(size: Int): Array<MessageData?> {
            return arrayOfNulls(size)
        }
    }

}