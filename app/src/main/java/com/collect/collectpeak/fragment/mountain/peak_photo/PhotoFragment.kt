package com.collect.collectpeak.fragment.mountain.peak_photo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentPhotoBinding
import com.collect.collectpeak.fragment.mountain.peak_time.CalendarData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.GlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType


class PhotoFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentPhotoBinding

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var targetCalendarData: CalendarData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {

        @JvmStatic
        fun newInstance(data : CalendarData) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable("data",data)
                }
            }
    }

    private val viewModel : PhotoViewModel by activityViewModels {
        PhotoViewModel.PhotoViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetCalendarData = it.getParcelable<CalendarData>("data") as CalendarData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_photo,container,false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        MichaelLog.i("PhotoFragment destroy")
    }

    override fun onStart() {
        super.onStart()

        viewModel.viewPagerLiveData.observe(this,{
            val adapter = PhotoAdapter()
            adapter.setData(it,fragmentActivity)
            dataBinding.photoViewPager.adapter = adapter
        })
    }

    private fun initView(root: View) {
        dataBinding.btnBack.setOnClickListener {
            if (fragmentActivity.supportFragmentManager.backStackEntryCount > 0){
                MichaelLog.i("可以退後")
                fragmentActivity.supportFragmentManager.popBackStack()
            }
        }

        dataBinding.photoSelectView.setOnClickListener {
            PictureSelector.create(fragmentActivity)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(3)
                .compress(true)
                .enableCrop(true)
                .hideBottomControls(false)
                .showCropFrame(false)
                .freeStyleCropEnabled(true)
                .forResult {
                    viewModel.onCatchPhotoListener(it,fragmentActivity.contentResolver)
                }
        }


    }



}