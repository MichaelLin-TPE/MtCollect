package com.collect.collectpeak.fragment.mountain.mt_list

import androidx.lifecycle.MutableLiveData
import com.collect.collectpeak.databinding.MtListItemLayoutBinding
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.tool.ImageLoaderHandler
import java.util.logging.Level

class MtListViewModel(
    private val dataBinding: MtListItemLayoutBinding,
    private val data : MountainData
) {

    val mtTitleLiveData = MutableLiveData(data.name)

    val mtHeightLiveData = MutableLiveData(data.height)

    val mtLevelLiveData = MutableLiveData(data.difficulty)

    init {
        ImageLoaderHandler.getInstance().setPhotoUrl(data.photo,dataBinding.mtItemPic)
    }


}