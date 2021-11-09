package com.collect.collectpeak.fragment.member.page_fragment.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.log.MichaelLog

class PostViewModel : ViewModel() {

    val showPostListLiveData = MutableLiveData<ArrayList<ShareData>>()

    val showDefaultView = MutableLiveData(true)

    fun onFragmentResume(uid : String) {
        FireStoreHandler.getInstance().getUserPostData(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<ShareData>>{
            override fun onCatchDataSuccess(data: ArrayList<ShareData>) {

                MichaelLog.i("取得貼文成功 : ${data.size}")

                val iterator = data.iterator()
                while (iterator.hasNext()){
                    val shareData = iterator.next()
                    if (shareData.uid != uid){
                        iterator.remove()
                    }
                }
                showPostListLiveData.value = data

                showDefaultView.value = data.isEmpty()

            }

            override fun onCatchDataFail() {

                MichaelLog.i("取得貼文失敗")

            }

        })
    }



    open class PostFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostViewModel() as T
        }
    }


}