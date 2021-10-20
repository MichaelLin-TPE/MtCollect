package com.collect.collectpeak.firebase

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MountainData : Serializable{

    @SerializedName("sid")
    var sid = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("height")
    var height: String = ""

    @SerializedName("day")
    var day: String = ""

    @SerializedName("content")
    var content: String = ""

    @SerializedName("location")
    var location: String = ""

    @SerializedName("difficulty")
    var difficulty: String = ""

    @SerializedName("check")
    var check: String = ""

    @SerializedName("photo")
    var photo: String = ""

    @SerializedName("time")
    var time: Long = 0
}