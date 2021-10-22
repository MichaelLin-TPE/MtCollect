package com.collect.collectpeak.tool

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.widget.TextView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.google.android.gms.common.SignInButton

class Tool {

    companion object {

        const val START_ACTIVITY = 1
        const val OUT_ACTIVITY = 2

        fun startActivityInAnim(activity: Activity, type: Int) {
            if (type == 1) {
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else if (type == 2) {
                activity.overridePendingTransition(R.anim.bottom_in, 0)
            }
        }

        fun startActivityOutAnim(activity: Activity, type: Int) {
            if (type == 1) {
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            } else if (type == 2) {
                activity.overridePendingTransition(0, R.anim.bottom_out)
            }
        }

        fun setGoogleButtonText(button: SignInButton, buttonName: String) {

            for (i in 0 until button.childCount) {

                val view = button.getChildAt(i)

                if (view is TextView) {

                    val tvText = view as TextView

                    tvText.text = buttonName

                }

            }

        }

        fun getScreenWidth() : Int {

            return Resources.getSystem().displayMetrics.widthPixels

        }

        fun getScreenHeight(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }


    }


}