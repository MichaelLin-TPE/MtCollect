package com.collect.collectpeak.tool

import android.view.View
import com.collect.collectpeak.fragment.equipment.EquipmentListViewModel
import com.collect.collectpeak.fragment.member.MemberViewModel
import com.collect.collectpeak.fragment.member.page_fragment.goal_detail.GoalDetailViewModel
import com.collect.collectpeak.fragment.member.page_fragment.goal_edit.GoalEditViewModel
import com.collect.collectpeak.fragment.member.page_fragment.post_detail.PostDetailViewModel
import com.collect.collectpeak.fragment.mountain.mt_list.MtViewModel

/**
 * 利用 DataBinding 來實作所有的 ClickListener
 */

class ButtonClickHandler<T>(private val viewModel:T) {


    /**
     * 編輯登頂資料 重新選擇山頭的登頂日期點擊事件
     */
    fun onGoalCalendarSelectListener(view: View){
        if (viewModel is GoalEditViewModel){
            viewModel.onGoalCalendarSelectListener()
        }
    }

    /**
     * 編輯登頂資料 重新選擇山頭的點擊事件
     */
    fun onMountainListClickListener(view: View){
       if (viewModel is GoalEditViewModel){
           viewModel.onEditMtListClickListener(view)
       }
    }

    /**
     * 編輯登頂頁面 編輯完成點擊事件
     */
    fun onEditPeakDataDoneButtonClickListener(view: View){
        if (viewModel is GoalEditViewModel){
            viewModel.buttonDoneClickListener()
        }
    }

    /**
     * 百岳列表頁面 選擇等級點擊事件
     */
    fun onLevelPickerClickListener(view:View){
        if(viewModel is MtViewModel){
            viewModel.onLevelSpinnerClickListener()
        }
    }

    /**
     * 裝備列表頁面 新增裝備列表點擊事件
     */
    fun onCreateEquipmentClickListener(view:View){
        if (viewModel is EquipmentListViewModel){
            viewModel.onCreateEquipmentClickListener()
        }
    }

    /**
     * 裝備列表頁面 顯示確定刪除所選的裝備視窗 點擊事件
     */
    fun onShowDeleteConfirmDialogClickListener(view: View){
        if (viewModel is EquipmentListViewModel){
            viewModel.onDeleteConfirmClickListener()
        }
    }

    /**
     * 裝備列表頁面 完成刪除頁面行動 點擊事件
     */
    fun onEquipmentListDoneButtonClickListener(view: View){
        if(viewModel is EquipmentListViewModel){
            viewModel.onDoneButtonClickListener()
        }
    }

    /**
     * 會員頁面 設定 ICON 點擊事件
     */
    fun onMemberSettingClickListener(view: View){
        if(viewModel is MemberViewModel){
            viewModel.onMemberSettingClickListener()
        }
    }
    /**
     * 所有的 BackButtonClickListener 都會使用這個方法
     */
    fun onBackButtonClickListener(view: View){
        if (viewModel is GoalDetailViewModel){
            viewModel.onBackButtonClickListener()
            return
        }
        if(viewModel is GoalEditViewModel){
            viewModel.onBackButtonClickListener()
            return
        }
        if(viewModel is PostDetailViewModel){
            viewModel.onBackButtonClickListener()
        }
    }


}