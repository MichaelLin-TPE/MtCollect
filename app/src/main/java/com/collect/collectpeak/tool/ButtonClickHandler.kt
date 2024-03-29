package com.collect.collectpeak.tool

import android.view.View
import com.collect.collectpeak.fragment.chat.ChatViewModel
import com.collect.collectpeak.fragment.equipment.EquipmentListViewModel
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentViewModel
import com.collect.collectpeak.fragment.home.HomeViewModel
import com.collect.collectpeak.fragment.member.MemberViewModel
import com.collect.collectpeak.fragment.member.page_fragment.goal_detail.GoalDetailViewModel
import com.collect.collectpeak.fragment.member.page_fragment.goal_edit.GoalEditViewModel
import com.collect.collectpeak.fragment.member.page_fragment.post_detail.PostDetailViewModel
import com.collect.collectpeak.fragment.member.page_fragment.post_edit.PostEditViewModel
import com.collect.collectpeak.fragment.message.MessageViewModel
import com.collect.collectpeak.fragment.mountain.mt_list.MtViewModel
import com.collect.collectpeak.fragment.notice.NoticeViewModel
import com.collect.collectpeak.fragment.user_page.UserPageViewModel

/**
 * 利用 DataBinding 來實作所有的 ClickListener
 */

class ButtonClickHandler<T>(private val viewModel:T) {

    /**
     * 點擊 裝備清單選擇完成
     */
    fun onEquipmentSelectFinishClickListener(view: View){
        if (viewModel is EquipmentViewModel){
            viewModel.onEquipmentSelectFinishClickListener()
        }
    }



    /**
     * 點擊 HOME 訊息
     */
    fun onMessageClickListener(view: View){
        if (viewModel is HomeViewModel){
            viewModel.onMessageClickListener()
        }
    }

    /**
     * 點擊HOME通知
     */
    fun onNoticeClickListener(view: View){
        if (viewModel is HomeViewModel){
            viewModel.onNoticeClickListener()
        }
    }

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
     * 編輯貼貼文葉面 編成完成點擊事件
     */
    fun onEditPostDataDoneButtonClickListener(view: View){
        if (viewModel is PostEditViewModel){
            viewModel.onDoneButtonClickListener()
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

        if(viewModel is EquipmentViewModel){
            viewModel.onBackButtonClickListener()
            return
        }

        if (viewModel is MessageViewModel){
            viewModel.onBackButtonClickListener()
            return
        }

        if (viewModel is NoticeViewModel){
            viewModel.onBackButtonClickListener()
            return
        }

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
            return
        }
        if (viewModel is PostEditViewModel){
            viewModel.onBackButtonClickListener()
        }
        if(viewModel is UserPageViewModel){
            viewModel.onBackPressClickListener()
        }
        if(viewModel is ChatViewModel){
            viewModel.onBackPressClickListener()
        }

    }

    /**
     * 發送聊天紀錄點擊事件
     */
    fun onSendMessageClickListener(view: View){
        if(viewModel is ChatViewModel){
            viewModel.onSendMessageClickListener()
        }
    }


}