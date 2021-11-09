package com.collect.collectpeak.fragment.user_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler

class UserPageViewModel : ViewModel() {

    private lateinit var onUserPageClickEventListener: OnUserPageClickEventListener

    val showUserPageLiveData = MutableLiveData<Boolean>()

    fun setOnUserPageClickEventListener(onUserPageClickEventListener: OnUserPageClickEventListener){
        this.onUserPageClickEventListener = onUserPageClickEventListener
    }



    fun onBackPressClickListener() {
        onUserPageClickEventListener.onBackClick()
    }

    fun onFragmentResume() {

        if (!AuthHandler.isLogin()){
            onUserPageClickEventListener.onBackClick()
            return
        }
        showUserPageLiveData.value = true
    }


    open class UserPageFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserPageViewModel() as T
        }
    }

}