package com.collect.collectpeak.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberData
import com.collect.collectpeak.log.MichaelLog

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    val memberDataLiveData = MutableLiveData<MemberData>()

    val toastLiveData = MutableLiveData<String>()

    val progressDialogLiveData = MutableLiveData<Boolean>()

    val dismissDialogLIveData = MutableLiveData<Boolean>()

    val finishPageLiveData = MutableLiveData<Boolean>()

    fun onFragmentStart() {
        repository.getUserProfile(object : FireStoreHandler.OnFireStoreCatchDataListener<MemberData>{
            override fun onCatchDataSuccess(data: MemberData) {
                MichaelLog.i("取得個人資料成功")
                memberDataLiveData.value = data

            }

            override fun onCatchDataFail() {
                MichaelLog.i("取得個人資料失敗")

            }

        })
    }

    fun onButtonDoneClickListener(name: String, description: String) {

        val memberData = memberDataLiveData.value as MemberData

        if (name.isEmpty()){
            toastLiveData.value = "名字不得為空"
            return
        }
        progressDialogLiveData.value = true

            memberData.name = name
        memberData.description = description

        repository.editMemberData(memberData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                MichaelLog.i("修正使用者資料成功")

                dismissDialogLIveData.value = true
                finishPageLiveData.value = true
            }

            override fun onCatchDataFail() {
                MichaelLog.i("修正使用者資料失敗")
                dismissDialogLIveData.value = true
                toastLiveData.value = "不明錯誤，請稍後再試。"
            }
        })
    }


    open class ProfileFactory(private val repository: ProfileRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ProfileViewModel(repository) as T
        }
    }

}