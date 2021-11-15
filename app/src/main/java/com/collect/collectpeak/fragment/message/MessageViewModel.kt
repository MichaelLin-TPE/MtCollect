package com.collect.collectpeak.fragment.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler

class MessageViewModel : ViewModel() {

    private lateinit var onMessageClickEventListener: OnMessageClickEventListener

    val showMessageListLiveData = MutableLiveData<ArrayList<MessageListData>>()

    val updateMessageListLiveData = MutableLiveData<ArrayList<MessageListData>>()

    fun setOnMessageClickEventListener(onMessageClickEventListener: OnMessageClickEventListener){
        this.onMessageClickEventListener = onMessageClickEventListener
    }

    fun onBackButtonClickListener() {
        onMessageClickEventListener.onBackPress()
    }

    fun onFragmentStart() {
        FireStoreHandler.getInstance().getMessageList(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MessageListData>>{
            override fun onCatchDataSuccess(data: ArrayList<MessageListData>) {
                showMessageListLiveData.value = data
                checkLastMessage(data)
            }

            override fun onCatchDataFail() {
                onMessageClickEventListener.onShowToast("發生不明錯誤，請稍後再試。")
            }

        })
    }

    private fun checkLastMessage(data: java.util.ArrayList<MessageListData>) {
        FireStoreHandler.getInstance().getLastMessage(data,object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MessageListData>>{
            override fun onCatchDataSuccess(data: ArrayList<MessageListData>) {
                updateMessageListLiveData.value = data
            }

            override fun onCatchDataFail() {
                onMessageClickEventListener.onShowToast("發生不明錯誤，請稍後再試。")
            }

        })
    }

    fun onMessageItemClickListener(data: MessageListData) {
        onMessageClickEventListener.goToChatPage(data.chatRoomId)
    }


    open class MessageFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessageViewModel() as T
        }
    }

}