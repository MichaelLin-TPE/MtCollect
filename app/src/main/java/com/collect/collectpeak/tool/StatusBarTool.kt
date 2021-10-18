package com.collect.collectpeak.tool

import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsControllerCompat
import com.collect.collectpeak.MtCollectorActivity
import com.collect.collectpeak.R

class StatusBarTool {

    companion object{

        fun setStatusBarSameColorAsActionBar(window : Window,view : View){
            window.statusBarColor =  Color.TRANSPARENT

            WindowInsetsControllerCompat(window,view).isAppearanceLightStatusBars = true
        }

    }

}