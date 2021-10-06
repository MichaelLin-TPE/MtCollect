package com.collect.collectpeak.custom_widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import com.collect.collectpeak.tool.TypeFaceHelper


class CollectPeakButton : androidx.appcompat.widget.AppCompatButton {
    constructor(context: Context?) : super(context!!) {
       typeface = TypeFaceHelper.get(context,"Jason.ttf")
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context!!, attrs) {
        typeface = TypeFaceHelper.get(context,"Jason.ttf")
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        typeface = TypeFaceHelper.get(context,"Jason.ttf")
    }


}