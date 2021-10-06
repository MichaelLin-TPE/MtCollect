package com.collect.collectpeak.tool

import android.util.TypedValue
import com.collect.collectpeak.MtCollectorApplication
import kotlin.math.roundToInt

class DpConvertTool {

    companion object {


        fun getDb(px: Int): Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            px.toFloat(),
            MtCollectorApplication.getInstance().resources.displayMetrics
        ).roundToInt()


    }

}