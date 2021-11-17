package com.collect.collectpeak.fragment.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.log.MichaelLog

class ShareViewModel(private val repository: ShareRepository) : ViewModel() {

    val postListLiveData = MutableLiveData<ArrayList<ShareData>>()

    val updatePostListLiveData = MutableLiveData<ArrayList<ShareData>>()

    val showDefaultViewLiveData = MutableLiveData(true)

    val showPostViewLiveData = MutableLiveData(false)

    val defaultContent = MutableLiveData("目前無任何貼文。")

    private val allPostData = ArrayList<ShareData>()

    private lateinit var onShareClickEventListener: OnShareClickEventListener

    fun setOnShareClickEventListener(onShareClickEventListener: OnShareClickEventListener){
        this.onShareClickEventListener = onShareClickEventListener
    }

    fun onFragmentResume() {

        if (!AuthHandler.isLogin()){
            showDefaultViewLiveData.value = true
            showPostViewLiveData.value = false
            defaultContent.value = "您尚未登入唷。"

            return
        }


        FireStoreHandler.getInstance().getUserPostData(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<ShareData>>{
            override fun onCatchDataSuccess(data: ArrayList<ShareData>) {
                allPostData.clear()
                showDefaultViewLiveData.value = false
                showPostViewLiveData.value = true
                allPostData.addAll(data)
                postListLiveData.value = data
            }

            override fun onCatchDataFail() {
                showDefaultViewLiveData.value = true
                showPostViewLiveData.value = false
               MichaelLog.i("無法取得 post data")
            }

        })
    }


    /**
     * 因為記憶體共用的關係 修改了 allPostData shareData會跟者一起改變
     */
    fun onHeartIconClick(shareData: ShareData) {
        for (data in allPostData) {
            if (data.shareId == shareData.shareId) {
                checkHeartData(data)
                break
            }
        }
        updatePostListLiveData.value = allPostData

        FireStoreHandler.getInstance().editPostData(shareData,
            object : FireStoreHandler.OnFireStoreCatchDataListener<Unit> {
                override fun onCatchDataSuccess(data: Unit) {
                    MichaelLog.i("修改貼文資料成功")
                }

                override fun onCatchDataFail() {
                    MichaelLog.i("修改貼文資料失敗")
                }
            })
    }

    private fun checkHeartData(data: ShareData) {

        val uid = AuthHandler.getCurrentUser()?.uid ?:return

        var isFoundHeartPressed = false
        for (likeData in data.likeArray) {
            if (likeData.uid == uid) {
                isFoundHeartPressed = true
                break
            }
        }
        if (!isFoundHeartPressed) {
            data.likeCount += 1
            val likeData = LikeData()
            likeData.uid = uid
            data.likeArray.add(likeData)
            MichaelLog.i("likeCount : ${data.likeCount} , likeArray size : ${data.likeArray.size}")
            return
        }
        data.likeCount -= 1
        for (likeData in data.likeArray) {
            if (likeData.uid == uid) {
                data.likeArray.remove(likeData)
                break
            }
        }
    }

    fun onUserPhotoCLickListener(uid: String) {
        if(uid == AuthHandler.getCurrentUser()?.uid){
            onShareClickEventListener.onGoToSelfPage()
            return
        }
        onShareClickEventListener.onGoToUserPage(uid)
    }


    open class ShareViewModelFactory(private val repository: ShareRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ShareViewModel(repository) as T
        }
    }

}