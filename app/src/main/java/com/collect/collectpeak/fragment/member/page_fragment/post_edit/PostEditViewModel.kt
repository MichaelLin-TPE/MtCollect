package com.collect.collectpeak.fragment.member.page_fragment.post_edit

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.page_fragment.goal_edit.GoalEditData
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.main_frame.OnBackButtonClickEventCallBackListener
import com.luck.picture.lib.entity.LocalMedia
import java.io.File
import java.lang.Exception

class PostEditViewModel : ViewModel() {

    val photoListLiveData = MutableLiveData<GoalEditData>()

    val updatePhotoListLiveData = MutableLiveData<GoalEditData>()
    val descriptionLiveData = MutableLiveData<String>()

    private lateinit var onButtonCallBackListener: OnBackButtonClickEventCallBackListener

    private lateinit var onPostEditClickEventListener: OnPostEditClickEventListener

    private lateinit var targetShareData: ShareData

    private val goalEditData = GoalEditData()

    fun setOnPostEditClickEventListener(onPostEditClickEventListener: OnPostEditClickEventListener){
        this.onPostEditClickEventListener = onPostEditClickEventListener
    }

    fun setOnBackButtonClickEventCallBackListener(onButtonCallBackListener: OnBackButtonClickEventCallBackListener){
        this.onButtonCallBackListener = onButtonCallBackListener
    }

    fun onBackButtonClickListener() {
        onButtonCallBackListener.onBackClick()
    }

    fun onFragmentResume(targetShareData: ShareData) {

        this.targetShareData = targetShareData



        goalEditData.photoArray = targetShareData.photoArray


        photoListLiveData.value = goalEditData

        descriptionLiveData.value = targetShareData.content
    }

    fun onCatchDescriptionDifferent(desc: String) {
        targetShareData.content = desc
    }

    fun onDoneButtonClickListener() {

        onPostEditClickEventListener.onShowProgressDialog()

        FireStoreHandler.getInstance().saveUserEditShareData(goalEditData,targetShareData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                onPostEditClickEventListener.onDismissProgressDialog()
                onPostEditClickEventListener.onFinishPage()

            }

            override fun onCatchDataFail() {
                onPostEditClickEventListener.onDismissProgressDialog()
                onPostEditClickEventListener.onShowToast("發生不知名錯誤,請稍後再試.")
            }

        })



    }

    fun onRemovePhotoByPhotoUrl(url: String) {
        var index = 0
        for (photoUrl in goalEditData.photoArray){
            if (photoUrl == url){
                goalEditData.photoArray.remove(photoUrl)
                break
            }
            index ++
        }
        updatePhotoListLiveData.value = goalEditData
    }

    fun onRemovePhotoByBitmap(position: Int) {
        goalEditData.newPhotoArray.removeAt(position)

        updatePhotoListLiveData.value = goalEditData
    }

    fun onAddPhotoClickListener() {
        val photoCount = 3 - goalEditData.photoArray.size - goalEditData.newPhotoArray.size

        MichaelLog.i("photoCount : $photoCount")
        onPostEditClickEventListener.onShowPictureSelector(photoCount)
    }

    fun onCatchPhotoResultListener(result: List<LocalMedia>, contentResolver: ContentResolver?) {
        result.forEach { data->

            val file = File(data.cutPath)
            val uri = Uri.fromFile(file)
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                goalEditData.newPhotoArray.add(bitmap)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        updatePhotoListLiveData.value = goalEditData
        MichaelLog.i("刪除照片")
    }


    open class PostEditFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostEditViewModel() as T
        }
    }

}