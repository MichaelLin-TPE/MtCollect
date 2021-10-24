package com.collect.collectpeak.fragment.mountain.peak_preview

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.StorageHandler
import com.collect.collectpeak.fragment.mountain.peak_time.MtPeakData
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.fragment.share.ShareFragment.Companion.PEAK_DATA
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PreviewViewModel : ViewModel() {

    val mtNameLiveData = MutableLiveData<String>()

    val mtLevelLiveData = MutableLiveData<String>()

    val mtTimeLiveData = MutableLiveData<String>()

    val showNoPhotoInfoLiveData = MutableLiveData(View.GONE)

    val mtPhotoArrayLiveData = MutableLiveData<ArrayList<Bitmap>>()

    val showConfirmDialogLiveData = MutableLiveData<String>()

    val showProgressDialogLiveData = MutableLiveData<String>()

    val dismissProgressDialogLiveData = MutableLiveData<Boolean>()

    val finishPageLiveData = MutableLiveData<Boolean>()

    val showToastLiveData = MutableLiveData<String>()

    private val photoUrlArray = ArrayList<String>()

    private lateinit var mtPeakData: MtPeakData

    private var uploadIndex = 0

    private var isSharePost = false


    fun onFragmentStart(mtPeakData: MtPeakData) {

        this.mtPeakData = mtPeakData

        mtNameLiveData.value = mtPeakData.mtName

        mtLevelLiveData.value = mtPeakData.level

        mtTimeLiveData.value =
            SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(mtPeakData.time))

        showNoPhotoInfoLiveData.value =
            if (mtPeakData.photoArray.isEmpty()) View.VISIBLE else View.GONE

        mtPhotoArrayLiveData.value = mtPeakData.photoArray
    }

    fun onDoneButtonClickListener(description: String) {

        mtPeakData.description = description

        showConfirmDialogLiveData.value = "此篇文是否發表至分享區？"
    }

    fun onConfirmShareClickListener() {
        showProgressDialogLiveData.value = "處理中"
        isSharePost = true
        Thread(uploadRunnable).start()
    }


    fun onCancelShareClickListener() {
        showProgressDialogLiveData.value = "處理中"
        isSharePost = false
        Thread(uploadRunnable).start()
    }

    private val uploadRunnable = {
        startToUploadPhoto()
    }


    private fun startToUploadPhoto() {

        if (uploadIndex < mtPeakData.photoArray.size){

            StorageHandler.uploadPeakPhoto(getByteArray(mtPeakData.photoArray[uploadIndex]),object : StorageHandler.OnCatchUploadPhotoUrlListener{
                override fun onCatchPhoto(url: String) {
                    uploadIndex ++
                    photoUrlArray.add(url)
                    startToUploadPhoto()
                }
            })
        }else{
            uploadIndex = 0
            val data = SummitData()
            data.mtLevel = mtPeakData.level
            data.mtName = mtPeakData.mtName
            data.mtTime = mtPeakData.time
            data.description = mtPeakData.description
            data.photoArray = photoUrlArray
            FireStoreHandler.getInstance().setUserSummitData(data,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
                override fun onCatchDataSuccess(data: Unit) {

                    if (isSharePost){
                        shareSummitDataToPost()
                        return
                    }

                    dismissProgressDialogLiveData.value = true

                    finishPageLiveData.value = true

                }

                override fun onCatchDataFail() {
                    dismissProgressDialogLiveData.value = true
                    showToastLiveData.value = "不知名錯誤,請稍後再試"
                }

            })


        }

    }


    /**
     * 分享至分享區
     */
    private fun shareSummitDataToPost() {

        val data = ShareData()

        data.content = "${mtPeakData.mtName},${mtPeakData.level},${mtPeakData.time},${mtPeakData.description}"

        data.type = PEAK_DATA

        data.uid = AuthHandler.getCurrentUser()?.uid ?: return

        data.photoArray = photoUrlArray

        FireStoreHandler.getInstance().setUserPostData(data,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {

                dismissProgressDialogLiveData.value = true
                finishPageLiveData.value = true

            }

            override fun onCatchDataFail() {

                dismissProgressDialogLiveData.value = true
                showToastLiveData.value = "不知名錯誤,請稍後再試"

            }
        })



    }

    private fun getByteArray (bitmap: Bitmap) : ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        val byteArray = stream.toByteArray()
        bitmap.recycle()
        return byteArray
    }

    fun onFragmentPause() {
        FireStoreHandler.getInstance().clear()
    }


    open class PreviewFactory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PreviewViewModel() as T
        }
    }

}