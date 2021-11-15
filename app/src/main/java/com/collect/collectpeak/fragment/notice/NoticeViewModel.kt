package com.collect.collectpeak.fragment.notice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import java.util.*
import kotlin.collections.ArrayList

class NoticeViewModel : ViewModel() {

    private lateinit var onNoticeClickEventListener: OnNoticeClickEventListener

    val showDefaultView = MutableLiveData(true)

    val showNotificationView = MutableLiveData(false)

    val showNotificationListView = MutableLiveData<ArrayList<NoticeData>>()

    private val noticeArray = ArrayList<NoticeData>()

    fun setOnNoticeClickEventListener(onNoticeClickEventListener: OnNoticeClickEventListener){
        this.onNoticeClickEventListener = onNoticeClickEventListener
    }

    fun onBackButtonClickListener() {
        onNoticeClickEventListener.onBackPress()
    }

    fun onFragmentResume() {
        FireStoreHandler.getInstance().getUserNotificationByUid(AuthHandler.getCurrentUser()?.uid,object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<NoticeData>>{
            override fun onCatchDataSuccess(data: ArrayList<NoticeData>) {
                noticeArray.clear()
                showDefaultView.value = false
                showNotificationView.value = true

                showNotificationListView.value = data

                noticeArray.addAll(data)

                var isNeedUpdate = false
                data.forEach {
                    if (!it.isCheck){
                        it.isCheck = true
                        isNeedUpdate = true
                    }
                }
                if (!isNeedUpdate){
                    return
                }
                val uid = AuthHandler.getCurrentUser()?.uid ?: return

                FireStoreHandler.getInstance().saveUserNotificationByUid(uid,data)

            }

            override fun onCatchDataFail() {
                showDefaultView.value = true
                showNotificationView.value = false
            }

        })
    }

    fun onApplyFriendAcceptListener(data: NoticeData) {
        FireStoreHandler.getInstance().clearApplyData(data)

        noticeArray.forEach {
            if (it.fromWho == data.fromWho && it.timeStamp == data.timeStamp && it.type == data.type){
                it.type = NoticeType.REQUEST_FRIEND_ACCEPT
                return@forEach
            }
        }
        val uid = AuthHandler.getCurrentUser()?.uid ?: return

        FireStoreHandler.getInstance().updateNoticeDataByUid(uid,noticeArray)


        //be friend
        val friendUid = data.fromWho
        val myUid = AuthHandler.getCurrentUser()?.uid.toString()

        FireStoreHandler.getInstance().addFriendByMyUid(friendUid)

        FireStoreHandler.getInstance().addFriendByOtherUid(friendUid,myUid)

        FireStoreHandler.getInstance().editBasicDataToAddFriendCountByUid(friendUid)

        FireStoreHandler.getInstance().editBasicDataToAddFriendCountByUid(myUid)

    }

    fun onApplyFriendRejectListener(data: NoticeData) {
        FireStoreHandler.getInstance().clearApplyData(data)

        noticeArray.forEach {
            if (it.fromWho == data.fromWho && it.timeStamp == data.timeStamp && it.type == data.type){
                it.type = NoticeType.REQUEST_FRIEND_REJECT
                return@forEach
            }
        }
        val uid = AuthHandler.getCurrentUser()?.uid ?: return

        FireStoreHandler.getInstance().updateNoticeDataByUid(uid,noticeArray)
    }

    open class NoticeFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NoticeViewModel() as T
        }
    }

}