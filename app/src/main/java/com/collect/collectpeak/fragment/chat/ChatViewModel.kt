package com.collect.collectpeak.fragment.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberData
import com.collect.collectpeak.log.MichaelLog

class ChatViewModel : ViewModel() {

    private lateinit var onChatClickEventListener: OnChatClickEventListener

    val actionBarTitleLiveData = MutableLiveData("")

    val messageListLiveData = MutableLiveData<ArrayList<MessageData>>()

    val editMessageLiveData = MutableLiveData("")

    private var targetMessage = ""

    private var targetChatId = ""

    fun setOnChatClickEventListener(onChatClickEventListener: OnChatClickEventListener) {
        this.onChatClickEventListener = onChatClickEventListener
    }

    fun onBackPressClickListener() {
        onChatClickEventListener.onBackPress()
    }

    fun onFragmentStart(targetChatId: String) {
        this.targetChatId = targetChatId
        val targetUid = targetChatId.replace(AuthHandler.getCurrentUser()?.uid.toString() , "")
        MichaelLog.i("targetChatId : $targetChatId , targetUid : $targetUid")
        //取得基本資料
        FireStoreHandler.getInstance().getUserProfileByUid(targetUid,
            object : FireStoreHandler.OnFireStoreCatchDataListener<MemberData> {
                override fun onCatchDataSuccess(data: MemberData) {
                    actionBarTitleLiveData.value =
                        if (data.name.isEmpty()) data.email else data.name
                }

                override fun onCatchDataFail() {
                    onChatClickEventListener.onShowToast("發生不知名錯誤，請稍後再試。")
                }
            })
        //取得聊天紀錄
        FireStoreHandler.getInstance().getChatRecord(targetChatId,
            object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MessageData>> {
                override fun onCatchDataSuccess(data: ArrayList<MessageData>) {
                    messageListLiveData.value = data
                }

                override fun onCatchDataFail() {

                }

            })
    }


    fun onSendMessageClickListener() {
        MichaelLog.i("點擊發送訊息")
        if (targetMessage.isEmpty()){
            onChatClickEventListener.onShowToast("說點甚麼吧....")
            return
        }
        editMessageLiveData.value = ""
        val data = MessageData()
        data.message = targetMessage
        data.uid = AuthHandler.getCurrentUser()?.uid.toString()
        data.timeStamp = System.currentTimeMillis()

        FireStoreHandler.getInstance().setMessageData(data,targetChatId)

    }

    fun onMessageTextChangeListener(message: String) {
        this.targetMessage = message
    }


    open class ChatFactory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatViewModel() as T
        }
    }

}