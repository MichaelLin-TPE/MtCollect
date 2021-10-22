package com.collect.collectpeak.fragment.mountain.mt_list

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData

interface MtRepository{
    fun getMountainList(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>)
    fun getLevelArray(): ArrayList<String>
    fun getUserSummitData(onCatchSummitDataListener: FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<SummitData>>)

}