package com.collect.collectpeak.fragment.mountain.peak_preview

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.StorageHandler
import com.collect.collectpeak.fragment.mountain.peak_time.MtPeakData
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

    private val photoUrlArray = ArrayList<String>()

    private lateinit var mtPeakData: MtPeakData

    private var uploadIndex = 0

    private var isSharePost = false

    private var description = ""


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

        this.description = description


        showConfirmDialogLiveData.value = "此篇文是否發表至分享區？"
    }

    fun onConfirmShareClickListener() {
        isSharePost = true
        startToUploadPhoto()
    }


    fun onCancelShareClickListener() {
        isSharePost = false
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
            data.photoArray = photoUrlArray



        }

    }
    private fun getByteArray (bitmap: Bitmap) : ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        val byteArray = stream.toByteArray()
        bitmap.recycle()
        return byteArray
    }


    open class PreviewFactory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PreviewViewModel() as T
        }
    }

}