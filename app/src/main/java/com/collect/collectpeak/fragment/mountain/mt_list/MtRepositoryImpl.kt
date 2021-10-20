package com.collect.collectpeak.fragment.mountain.mt_list

import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.MountainData

class MtRepositoryImpl : MtRepository {
    override fun getMountainList(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>) {

        FireStoreHandler.getInstance().getMountainList(object : FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<MountainData>>{
            override fun onCatchDataSuccess(data: ArrayList<MountainData>) {
                onFireStoreCatchDataListener.onCatchDataSuccess(data)
            }

            override fun onCatchDataFail() {
                onFireStoreCatchDataListener.onCatchDataFail()
            }

        })

    }

    override fun getLevelArray(): ArrayList<String> = ArrayList<String>().apply {
        val context = MtCollectorApplication.getInstance().getContext()
        this.add(context.getString(R.string.level_all))
        this.add(context.getString(R.string.level_a))
        this.add(context.getString(R.string.level_b))
        this.add(context.getString(R.string.level_c))
        this.add(context.getString(R.string.level_d))
        this.add(context.getString(R.string.level_e))
    }

}