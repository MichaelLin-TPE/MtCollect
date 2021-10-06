package com.collect.collectpeak.tool

import java.text.SimpleDateFormat
import java.util.*

class TimeTool {

    companion object{
        fun getCurrentDate():String = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(System.currentTimeMillis()))

        fun getCurrentMillis():Long = System.currentTimeMillis()
    }
}