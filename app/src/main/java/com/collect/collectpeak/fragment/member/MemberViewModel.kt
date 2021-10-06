package com.collect.collectpeak.fragment.member

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.GoogleLoginTool
import kotlin.coroutines.coroutineContext

class MemberViewModel(private val repository: MemberRepository) : ViewModel() {


    val defaultLoginViewLiveData = MutableLiveData<Boolean>()

    val memberInfoViewLiveData = MutableLiveData<Boolean>()

    val photoSelectLiveData = MutableLiveData<Boolean>()

    val loadingDialogLiveData = MutableLiveData<String>()

    val dismissLoadingDialog = MutableLiveData<Boolean>()

    val updateMemberViewLiveData = MutableLiveData<Boolean>()

    private val targetUser = AuthHandler.getCurrentUser()

    fun onFragmentStart() {

        defaultLoginViewLiveData.value = !AuthHandler.isLogin()
        memberInfoViewLiveData.value = AuthHandler.isLogin()

    }

    fun onGoogleLoginClickListener() {
        GoogleLoginTool.startToLoginFlow()
    }

    fun onLoginFail() {

    }

    fun onLoginSuccess() {

        defaultLoginViewLiveData.value = !AuthHandler.isLogin()

        memberInfoViewLiveData.value = true

    }

    fun onLoginSuccessAndNeedEditProfile() {

        defaultLoginViewLiveData.value = !AuthHandler.isLogin()

        memberInfoViewLiveData.value = true




    }

    fun onPhotoSelectListener() {
        photoSelectLiveData.value = true
    }

    fun onShowLoadingDialog(content: String) {
        loadingDialogLiveData.value = content
    }

    fun onUploadPhoto(bytes: ByteArray) {
        repository.onUpdateLoadPhoto(bytes, object : MemberRepository.OnUploadPhotoListener {
            override fun onSuccess() {
                MichaelLog.i("照片上傳成功")
                dismissLoadingDialog.value = true
                updateMemberViewLiveData.value = true
            }

            override fun onFail() {

                MichaelLog.i("照片上傳失敗")
                dismissLoadingDialog.value = true

            }

        })
    }


    open class MemberViewModelFactory(private val repository: MemberRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MemberViewModel(repository) as T
        }
    }
}