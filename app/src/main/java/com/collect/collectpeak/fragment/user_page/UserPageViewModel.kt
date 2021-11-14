package com.collect.collectpeak.fragment.user_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.FireStoreHandler.Companion.INVITING
import com.collect.collectpeak.firebase.FireStoreHandler.Companion.NO_FRIEND
import com.collect.collectpeak.fragment.notice.NoticeData
import com.collect.collectpeak.fragment.notice.NoticeType
import com.collect.collectpeak.log.MichaelLog
import org.json.JSONObject
import java.lang.Exception

class UserPageViewModel : ViewModel() {

    private lateinit var onUserPageClickEventListener: OnUserPageClickEventListener

    val showUserPageLiveData = MutableLiveData<JSONObject>()

    val updateFollowButtonLiveData = MutableLiveData<JSONObject>()

    private var targetChatId = ""

    fun setOnUserPageClickEventListener(onUserPageClickEventListener: OnUserPageClickEventListener){
        this.onUserPageClickEventListener = onUserPageClickEventListener
    }

    fun onBackPressClickListener() {
        onUserPageClickEventListener.onBackClick()
    }

    fun onFragmentResume(targetUid: String) {

        if (!AuthHandler.isLogin()){
            onUserPageClickEventListener.onBackClick()
            return
        }

        FireStoreHandler.getInstance().getUserFriendApplyData(targetUid,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
            override fun onCatchDataSuccess(data: String) {
                if (data == INVITING){
                    showUserPageLiveData.value = getFollowButtonJSONObject("邀請中",false)
                    return
                }
                if(data == NO_FRIEND){
                    showUserPageLiveData.value = getFollowButtonJSONObject("追蹤",true)
                    return
                }
                showUserPageLiveData.value = getFollowButtonJSONObject("已追蹤",false)
            }

            override fun onCatchDataFail() {
                onUserPageClickEventListener.onShowToast("發生不知名錯誤，請稍後再試。")
                showUserPageLiveData.value = getFollowButtonJSONObject("追蹤",true)
            }

        })

        MichaelLog.i("檢查是否有聊天室")
        FireStoreHandler.getInstance().createChatRoom(targetUid,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
            override fun onCatchDataSuccess(data: String) {
               targetChatId = data
            }

            override fun onCatchDataFail() {

            }

        })


    }



    fun onFollowClickListener(targetUid: String) {

        FireStoreHandler.getInstance().applyToBeFriend(targetUid,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                updateFollowButtonLiveData.value = getFollowButtonJSONObject("邀請中",false)
            }

            override fun onCatchDataFail() {
                onUserPageClickEventListener.onShowToast("發生不知名錯誤，請稍後再試。")
            }

        })

        MichaelLog.i("發送通知")
        val data = NoticeData()
        data.type = NoticeType.REQUEST_FRIEND
        data.fromWho = AuthHandler.getCurrentUser()?.uid.toString()
        data.isCheck = false
        data.timeStamp = System.currentTimeMillis()
        FireStoreHandler.getInstance().sendNotification(targetUid,data)

    }

    private fun getFollowButtonJSONObject(buttonText: String, isEnable: Boolean): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("enable",isEnable)
            jsonObject.put("text",buttonText)
        }catch (e : Exception){
            e.printStackTrace()
        }
        return jsonObject
    }

    fun onMessageClickListener() {

        onUserPageClickEventListener.onGoToUserChatPage(targetChatId)

    }

    open class UserPageFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserPageViewModel() as T
        }
    }

}