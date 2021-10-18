package com.collect.collectpeak.fragment.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler

class SettingViewModel : ViewModel() {

    val finishPageLiveData = MutableLiveData<Boolean>()

    val showLogoutConfirmDialogLiveData = MutableLiveData<String>()

    fun onLogoutClickListener() {

        showLogoutConfirmDialogLiveData.value = "確定要登出嗎？"

    }

    fun onLogoutConfirmClickListener() {

        AuthHandler.logout()

        finishPageLiveData.value = true

    }


    open class SettingViewModelFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingViewModel() as T
        }
    }

}