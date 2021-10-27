package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.TempDataHandler
import com.luck.picture.lib.entity.LocalMedia
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    private val mountainList = ArrayList<MountainData>()

    fun onFragmentStart(targetSummitData: SummitData) {

        //這邊會先拿資料
        FireStoreHandler.getInstance().getMountainList(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>{
            override fun onCatchDataSuccess(data: ArrayList<MountainData>) {
                mountainList.addAll(data)
            }

            override fun onCatchDataFail() {
            }
        })


        this.targetSummitData = targetSummitData

        targetEditData.photoArray = targetSummitData.photoArray

        mtNameLiveData.value = "山名 : "+targetSummitData.mtName

        mtLevelLiveData.value = "等級 : "+targetSummitData.mtLevel

        val time = if (TempDataHandler.getUserSelectTimeStamp() != 0L) SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(Date(TempDataHandler.getUserSelectTimeStamp()))
                else SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(targetSummitData.mtTime))

        this.targetSummitData.mtTime = if (TempDataHandler.getUserSelectTimeStamp() != 0L) TempDataHandler.getUserSelectTimeStamp() else targetSummitData.mtTime


        mtTimeLiveData.value = "時間 : $time"

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

    fun buttonDoneClickListener() {

    }


    open class GoalEditFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalEditViewModel() as T
        }
    }
}