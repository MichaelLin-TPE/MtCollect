package com.collect.collectpeak.fragment.member.page_fragment.post_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.main_frame.OnBackButtonClickEventCallBackListener

class PostDetailViewModel : ViewModel() {


    private lateinit var onBackButtonClickEventCallBackListener: OnBackButtonClickEventCallBackListener

    private lateinit var onPostDetailClickEventListener: OnPostDetailClickEventListener

    val showPostListLiveData = MutableLiveData<ArrayList<ShareData>>()

    private val allPostData = ArrayList<ShareData>()

    fun setOnPostDetailClickEventListener(onPostDetailClickEventListener: OnPostDetailClickEventListener){
        this.onPostDetailClickEventListener = onPostDetailClickEventListener
    }

    fun setOnBackButtonClickEventCallBackListener(onBackButtonClickEventCallBackListener: OnBackButtonClickEventCallBackListener){
        this.onBackButtonClickEventCallBackListener = onBackButtonClickEventCallBackListener
    }

    fun onBackButtonClickListener() {
        onBackButtonClickEventCallBackListener.onBackClick()
    }

    fun onFragmentResume(targetShareData: ShareData) {

        FireStoreHandler.getInstance().getUserPostData(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<ShareData>>{
            override fun onCatchDataSuccess(data: ArrayList<ShareData>) {
                MichaelLog.i("取得貼文成功：${data.size}")
                val iterator = data.iterator()
                while (iterator.hasNext()){
                    val shareData = iterator.next()
                    if (shareData.uid != AuthHandler.getCurrentUser()?.uid){
                        iterator.remove()
                    }
                }
                allPostData.addAll(data)
                showPostListLiveData.value = data

            }

            override fun onCatchDataFail() {
                MichaelLog.i("取得貼文失敗")
            }

        })

    }

    fun onSettingClickListener(shareData: ShareData) {
        onPostDetailClickEventListener.onClickSetting(shareData)
    }

    fun onDeletePostClickListener(shareData: ShareData) {
        onPostDetailClickEventListener.onShowConfirmDeleteDialog(shareData)
    }

    fun onDeletePostConfirmListener(shareData: ShareData) {
        onPostDetailClickEventListener.onShowProgressDialog()

        FireStoreHandler.getInstance().removeShareData(shareData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                onPostDetailClickEventListener.onDismissProgressDialog()
                for (allData in allPostData){
                    if(allData.shareId == shareData.shareId){
                        allPostData.remove(allData)
                        break
                    }
                }
                showPostListLiveData.value = allPostData
            }

            override fun onCatchDataFail() {
                onPostDetailClickEventListener.onDismissProgressDialog()
            }

        })

    }


    open class PostDetailFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostDetailViewModel() as T
        }
    }

}