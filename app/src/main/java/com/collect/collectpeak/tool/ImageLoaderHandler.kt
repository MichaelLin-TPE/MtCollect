package com.collect.collectpeak.tool

import android.graphics.Bitmap
import android.widget.ImageView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class ImageLoaderHandler {

    private lateinit var options : DisplayImageOptions

    private lateinit var imageLoader: ImageLoader

    companion object{
        private val instance = ImageLoaderHandler()

        fun getInstance():ImageLoaderHandler{
            return instance
        }
    }

    fun initImageLoader(){
        imageLoader = ImageLoader.getInstance()
        options = DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.hiking_logo)
            .showImageOnFail(R.drawable.hiking_logo)
            .showImageOnLoading(R.drawable.hiking_logo)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

        val config = ImageLoaderConfiguration.Builder(MtCollectorApplication.getInstance().getContext())
            .defaultDisplayImageOptions(options).build()
        imageLoader.init(config)
    }


    fun setPhotoUrl(url:String,ivIcon : ImageView){
        imageLoader.displayImage(url,ivIcon)
    }



}