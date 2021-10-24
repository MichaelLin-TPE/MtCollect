package com.collect.collectpeak.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import com.collect.collectpeak.R
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool

class GoalSettingDialog {


    private lateinit var mask: View

    private lateinit var rootView: ConstraintLayout

    private lateinit var settingDialog: View

    private var screenBottom = 0

    private var settingDialogY = 0

    private lateinit var onSettingDialogItemClickListener: OnSettingDialogItemClickListener

    fun setOnSettingDialogItemClickListener(onSettingDialogItemClickListener: OnSettingDialogItemClickListener){
        this.onSettingDialogItemClickListener = onSettingDialogItemClickListener
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val instance = GoalSettingDialog()
    }

    fun showDialog(mask: View, rootView: ConstraintLayout, context: Context) {

        this.mask = mask

        this.rootView = rootView

        val settingDialog = View.inflate(context, R.layout.goal_setting_dialog_layout, null)

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        settingDialog.layoutParams = params

        this.settingDialog = settingDialog

        mask.visibility = View.VISIBLE

        rootView.addView(settingDialog)

        settingDialog.post {

            screenBottom = rootView.height

            settingDialogY = Tool.getScreenHeight() - settingDialog.height

            startAnimation(true)

        }

        mask.setOnClickListener {

            startAnimation(false)

        }


    }

    private fun startAnimation(isShow: Boolean) {

        settingDialog.clearAnimation()


        settingDialog.visibility = View.VISIBLE

        val animation = if (isShow) {
            TranslateAnimation(0f, 0f, screenBottom.toFloat(), settingDialogY.toFloat())
        } else {
            TranslateAnimation(0f, 0f, 0f, screenBottom.toFloat())
        }

        animation.duration = 200

        animation.repeatCount = 0

        animation.fillAfter = true

        settingDialog.animation = animation

        val editView = settingDialog.findViewById<View>(R.id.setting_edit_view)

        val deleteView = settingDialog.findViewById<View>(R.id.setting_delete_view)

        editView.setOnClickListener {
            onSettingDialogItemClickListener.onEditClick()
            startAnimation(false)
        }

        deleteView.setOnClickListener {
            onSettingDialogItemClickListener.onDeleteClick()
            startAnimation(false)
        }

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                MichaelLog.i("結束動畫 : " + settingDialog.y)
                settingDialog.y = settingDialogY.toFloat()

                settingDialog.clearAnimation()

                if (!isShow) {
                    rootView.removeView(settingDialog)
                    mask.visibility = View.GONE
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

    }

    interface OnSettingDialogItemClickListener{
        fun onEditClick()

        fun onDeleteClick()
    }


}