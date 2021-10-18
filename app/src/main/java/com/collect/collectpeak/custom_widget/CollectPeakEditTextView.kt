package com.collect.collectpeak.custom_widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.collect.collectpeak.tool.TypeFaceHelper


class CollectPeakEditTextView : AppCompatEditText {
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