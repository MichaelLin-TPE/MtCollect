package com.collect.collectpeak.fragment.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberData

class ChatViewModel : ViewModel() {

    private lateinit var onChatClickEventListener: OnChatClickEventListener

    val actionBarTitleLiveData = MutableLiveData("")

    fun setOnChatClickEventListener(onChatClickEventListener: OnChatClickEventListener){
        this.onChatClickEventListener = onChatClickEventListener
    }

    fun onBackPressClickListener() {
        onChatClickEventListener.onBackPress()
    }

    fun onFragmentStart(targetChatId: String) {

        var targetUid = ""
        val uidArray = targetChatId.split("&")
        uidArray.forEach {
            if (it != AuthHandler.getCurrentUser()?.uid){
                targetUid = it
                return@forEach
            }
        }

        FireStoreHandler.getInstance().getUserProfileByUid(targetUid,object : FireStoreHandler.OnFireStoreCatchDataListener<MemberData>{
            override fun onCatchDataSuccess(data: MemberData) {
                actionBarTitleLiveData.value = if (data.name.isEmpty()) data.email else data.name
            }

            override fun onCatchDataFail() {
                onChatClickEventListener.onShowToast("發生不知名錯誤，請稍後再試。")
            }
        })






    }


    open class ChatFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatViewModel() as T
        }
    }

}