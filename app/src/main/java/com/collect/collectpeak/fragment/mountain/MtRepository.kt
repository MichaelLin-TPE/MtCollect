package com.collect.collectpeak.fragment.mountain

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData

interface MtRepository{
    fun getMountainList(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>)
    fun getLevelArray(): ArrayList<String>

}