package com.collect.collectpeak.tool

import android.view.View
import com.collect.collectpeak.fragment.member.page_fragment.goal_edit.GoalEditViewModel

class ButtonClickHandler<T>(private val viewModel:T) {

    fun onMountainListClickListener(view: View){
       if (viewModel is GoalEditViewModel){
           viewModel.onEditMtListClickListener(view)
       }
    }

    fun onEditPeakDataDoneButtonClickListener(view: View){
        if (viewModel is GoalEditViewModel){
            viewModel.buttonDoneClickListener()
        }

    }

}