package com.collect.collectpeak.fragment.mountain.peak_photo

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.fragment.mountain.peak_time.MtPeakData
import com.luck.picture.lib.entity.LocalMedia
import java.io.File
import java.lang.Exception

class PhotoViewModel : ViewModel() {

    val showDefaultViewLiveData = MutableLiveData(View.VISIBLE)

    val showViewPager = MutableLiveData(View.INVISIBLE)

    val showReSelectButtonLiveData = MutableLiveData(View.GONE)

    private val bitmapArray = ArrayList<Bitmap>()

    val viewPagerLiveData = MutableLiveData<ArrayList<Bitmap>>()

    val goToPreviewPageLiveData = MutableLiveData<MtPeakData>()

    private lateinit var mtPeakData: MtPeakData

    private var uploadCount = 0

    fun onCatchPhotoListener(it: List<LocalMedia>, contentResolver: ContentResolver) {
        showDefaultViewLiveData.value = View.GONE
        showViewPager.value = View.VISIBLE
        bitmapArray.clear()
        it.forEach { data->

            val file = File(data.cutPath)
            val uri = Uri.fromFile(file)
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                bitmapArray.add(bitmap)
            }catch (e : Exception){
                e.printStackTrace()
            }

        }
        showReSelectButtonLiveData.value = View.VISIBLE
        viewPagerLiveData.value = bitmapArray


    }

    fun onNextButtonClickListener() {

        mtPeakData.photoArray = bitmapArray

        goToPreviewPageLiveData.value = mtPeakData

    }

    fun onFragmentStart(targetMtPeakData: MtPeakData) {
        this.mtPeakData = targetMtPeakData
    }

    fun onIgnoreButtonClickListener() {
        mtPeakData.photoArray = ArrayList()
        goToPreviewPageLiveData.value = mtPeakData
    }


    open class PhotoViewModelFactory : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PhotoViewModel() as T
        }
    }

}