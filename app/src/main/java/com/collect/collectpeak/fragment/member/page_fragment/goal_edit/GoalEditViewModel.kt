package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.OnButtonCallBackListener
import com.collect.collectpeak.tool.TempDataHandler
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    private lateinit var originalSummitData: SummitData

    private val targetEditData = GoalEditData()

    private val mountainList = ArrayList<MountainData>()

    private lateinit var onMtListButtonClickListener : OnButtonCallBackListener<String>

    fun setOnMtListButtonClickListener(onMtListButtonClickListener : OnButtonCallBackListener<String>){
        this.onMtListButtonClickListener = onMtListButtonClickListener
    }

    fun onFragmentStart(targetSummitData: SummitData) {

        //這邊會先拿資料
        FireStoreHandler.getInstance().getMountainList(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>{
            override fun onCatchDataSuccess(data: ArrayList<MountainData>) {
                mountainList.addAll(data)
            }

            override fun onCatchDataFail() {
            }
        })


        this.originalSummitData = targetSummitData

        targetEditData.photoArray = this.originalSummitData.photoArray

        mtNameLiveData.value = "山名 : "+this.originalSummitData.mtName

        mtLevelLiveData.value = "等級 : "+this.originalSummitData.mtLevel

        val time = if (TempDataHandler.getUserSelectTimeStamp() != 0L) SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(Date(TempDataHandler.getUserSelectTimeStamp()))
                else SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(targetSummitData.mtTime))

        this.originalSummitData.mtTime = if (TempDataHandler.getUserSelectTimeStamp() != 0L) TempDataHandler.getUserSelectTimeStamp() else targetSummitData.mtTime


        mtTimeLiveData.value = "時間 : $time"

        mtDescLiveData.value = this.originalSummitData.description

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
        MichaelLog.i("點擊buttonDone")
        onMtListButtonClickListener.showLoadingDialog("編輯中")

        FireStoreHandler.getInstance().saveUserEditSummitData(targetEditData,originalSummitData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                onMtListButtonClickListener.dismissLoadingDialog()

                onMtListButtonClickListener.finishPage()

            }

            override fun onCatchDataFail() {
                onMtListButtonClickListener.dismissLoadingDialog()

                onMtListButtonClickListener.showToast("發生不知名錯誤，請稍後再試")

            }
        })

    }




    fun onEditMtListClickListener(view: View) {
        MichaelLog.i("點擊 editList")
        onMtListButtonClickListener.onCatchResult(originalSummitData.mtName)
    }


    fun onPeakSelectClickListener(peak: String, level: String) {

        originalSummitData.mtName = peak
        originalSummitData.mtLevel = level

        mtNameLiveData.value = "山名 : "+originalSummitData.mtName

        mtLevelLiveData.value = "等級 : "+originalSummitData.mtLevel
    }

    fun onCatchDescription(desc: String) {
        originalSummitData.description = desc
    }


    open class GoalEditFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GoalEditViewModel() as T
        }
    }
}