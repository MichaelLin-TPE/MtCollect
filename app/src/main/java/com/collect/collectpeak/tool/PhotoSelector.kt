package com.collect.collectpeak.tool

import android.app.Activity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia

class PhotoSelector {

    companion object{
        val instance = PhotoSelector()
    }
    fun showPhotoSelectorView(activity : Activity,onCatchPhotoFromSelectListener: OnCatchPhotoFromSelectListener){

        PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(3)
            .compress(true)
            .enableCrop(true)
            .hideBottomControls(false)
            .showCropFrame(false)
            .freeStyleCropEnabled(true)
            .forResult {
                onCatchPhotoFromSelectListener.onCatchPhoto(it)
            }

    }

    fun showPhotoSelectorView(activity : Activity,photoCount:Int,onCatchPhotoFromSelectListener: OnCatchPhotoFromSelectListener){

        PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(photoCount)
            .compress(true)
            .enableCrop(true)
            .hideBottomControls(false)
            .showCropFrame(false)
            .freeStyleCropEnabled(true)
            .forResult {
                onCatchPhotoFromSelectListener.onCatchPhoto(it)
            }

    }

    fun interface OnCatchPhotoFromSelectListener{
        fun onCatchPhoto(result : List<LocalMedia>)
    }




}