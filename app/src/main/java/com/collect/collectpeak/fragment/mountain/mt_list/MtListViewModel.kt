package com.collect.collectpeak.fragment.mountain.mt_list

import androidx.lifecycle.MutableLiveData
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.MtListItemLayoutBinding
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ImageLoaderHandler
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level

class MtListViewModel(
    private val dataBinding: MtListItemLayoutBinding,
    private val data : MountainData
) {

    private lateinit var onMountainFootPrintClickListener: OnMountainFootPrintClickListener

    val mtTitleLiveData = MutableLiveData(data.name)

    val mtHeightLiveData = MutableLiveData(data.height)

    val mtLevelLiveData = MutableLiveData(data.difficulty)

    val mtFootPrintIconLiveData = MutableLiveData(false)

    val mtTimeLiveData = MutableLiveData<String>().apply {

        if (data.time == 0L){
            mtFootPrintIconLiveData.value = false
            return@apply
        }
        MichaelLog.i("顯示時間")
        this.value = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(data.time))
        mtFootPrintIconLiveData.value = true
    }

    fun setOnMountainFootPrintClickListener(onMountainFootPrintClickListener: OnMountainFootPrintClickListener){
        this.onMountainFootPrintClickListener = onMountainFootPrintClickListener
    }

    init {
        ImageLoaderHandler.getInstance().setPhotoUrl(data.photo,dataBinding.mtItemPic)

        if (data.time == 0L){
            dataBinding.mtItemFootPrint.setOnClickListener {

                onMountainFootPrintClickListener.onMtFootPrintClick(data)

            }
        }

    }


    interface OnMountainFootPrintClickListener{
        fun onMtFootPrintClick(data : MountainData)
    }


}