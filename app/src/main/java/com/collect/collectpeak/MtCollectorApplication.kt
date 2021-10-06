package com.collect.collectpeak

import android.app.Application
import android.content.Context
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.tool.ImageLoaderHandler
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MtCollectorApplication : Application() {

    companion object{
        private var instance : MtCollectorApplication = MtCollectorApplication()

        @JvmName("getInstance1")
        fun getInstance():MtCollectorApplication{
            return instance
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //初始化 Comment
        val db = Firebase.firestore
        FireStoreHandler.getInstance().initFireStore(db)

        //初始化 ImageLoader
        ImageLoaderHandler.getInstance().initImageLoader()

    }

    fun getContext():Context  = this

}