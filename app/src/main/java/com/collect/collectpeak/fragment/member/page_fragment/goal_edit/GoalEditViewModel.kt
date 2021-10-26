package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.luck.picture.lib.entity.LocalMedia
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class GoalEditViewModel : ViewModel() {


    val mtNameLiveData = MutableLiveData<String>()

    val mtLevelLiveData = MutableLiveData<String>()

    val mtTimeLiveData = MutableLiveData<String>()

    val mtDescLiveData = MutableLiveData<String>()

    val mtPhotoListLivData = MutableLiveData<GoalEditData>()

    val updatePhotoListLiveData = MutableLiveData<GoalEditData>()

    val showPhotoSelectorViewLiveData = MutableLiveData<Int>()

    private lateinit var targetSummitData: SummitData

    private val targetEditData = GoalEditData()

    fun onFragmentStart(targetSummitData: SummitData) {

        this.targetSummitData = targetSummitData

        targetEditData.photoArray = targetSummitData.photoArray

        mtNameLiveData.value = "山名 : "+targetSummitData.mtName

        mtLevelLiveData.value = "等級 : "+targetSummitData.mtLevel

        mtTimeLiveData.value = "時間 : ${SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(targetSummitData.mtTime))}"

        mtDescLiveData.value = targetSummitData.description

        mtPhotoListLivData.value = targetEditData

    }

    fun onPhotoRemoveClickListener(url: String) {
        var index = 0
        for (photoUrl in targetEditData.photoArray){
            if (photoUrl == url){
                targetEditData.photoArray.remove(photoUrl)
                break
            }
            index ++
        }
        updatePhotoListLiveData.value = targetEditData

    }

    fun onAddPhotoClickListener() {

        val photoCount = 3 - targetEditData.photoArray.size - targetEditData.newPhotoArray.size

        MichaelLog.i("photoCount : $photoCount")

        showPhotoSelectorViewLiveData.value = photoCount
    }

    fun onCatchPhotoResultListener(result: List<LocalMedia>, contentResolver: ContentResolver) {
        result.forEach { data->

            val file = File(data.cutPath)
            val uri = Uri.fromFile(file)
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                targetEditData.newPhotoArray.add(bitmap)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        updatePhotoListLiveData.value = targetEditData
        MichaelLog.i("刪除照片")
    }

    fun onPhotoRemoveBitmapClickListener(position: Int) {

        targetEditData.newPhotoArray.removeAt(position)

        updatePhotoListLiveData.value = targetEditData

    }


    open class GoalEditFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalEditViewModel() as T
        }
    }
}