package com.collect.collectpeak.fragment.share

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

class ShareData() : Parcelable{

    var type = 0

    var photoArray = ArrayList<String>()

    var likeCount = 0

    var likeArray = ArrayList<LikeData>()

    var messageCount = 0

    var messageArray = ArrayList<MessageData>()

    var content = ""

    var uid = ""

    var shareId = UUID.randomUUID().toString()

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
        likeCount = parcel.readInt()
        messageCount = parcel.readInt()
        content = parcel.readString().toString()
        uid = parcel.readString().toString()
        shareId = parcel.readString().toString()
        parcel.createStringArrayList().let {
            if (it == null){
                return@let
            }
            photoArray = it
        }
        parcel.createTypedArrayList(MessageData.CREATOR).let{
            if (it == null){
                return@let
            }
            messageArray = it
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeInt(likeCount)
        parcel.writeInt(messageCount)
        parcel.writeString(content)
        parcel.writeString(uid)
        parcel.writeString(shareId)
        parcel.writeStringList(photoArray)
        parcel.writeTypedList(messageArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShareData> {
        override fun createFromParcel(parcel: Parcel): ShareData {
            return ShareData(parcel)
        }

        override fun newArray(size: Int): Array<ShareData?> {
            return arrayOfNulls(size)
        }
    }


}