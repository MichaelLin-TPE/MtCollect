package com.collect.collectpeak.custom_widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.collect.collectpeak.R
import com.collect.collectpeak.tool.TypeFaceHelper


class CollectPeakTextView : AppCompatTextView {
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