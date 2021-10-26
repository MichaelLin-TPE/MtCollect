package com.collect.collectpeak.fragment.mountain.peak_photo

import android.content.Context
import android.content.Intent
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
import com.collect.collectpeak.fragment.mountain.peak_preview.PreviewFragment
import com.collect.collectpeak.fragment.mountain.peak_time.MtPeakData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.FragmentUtil.Companion.ANIM_LEFT_RIGHT
import com.collect.collectpeak.tool.GlideEngine
import com.collect.collectpeak.tool.PhotoSelector
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType


class PhotoFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentPhotoBinding

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var targetMtPeakData: MtPeakData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {

        @JvmStatic
        fun newInstance(data : MtPeakData) =
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
            targetMtPeakData = it.getParcelable<MtPeakData>("data") as MtPeakData
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

    override fun onPause() {
        super.onPause()
        viewModel.goToPreviewPageLiveData.value = MtPeakData()
        viewModel.goToPreviewPageLiveData.removeObservers(this)
    }

    override fun onStart() {
        super.onStart()

        viewModel.onFragmentStart(targetMtPeakData)

        viewModel.viewPagerLiveData.observe(this,{
            val adapter = PhotoAdapter()
            adapter.setData(it,fragmentActivity)
            dataBinding.photoViewPager.adapter = adapter
        })

        viewModel.goToPreviewPageLiveData.observe(this,{
            if (it.mtName.isEmpty()){
                return@observe
            }
            FragmentUtil.replace(R.id.container,fragmentActivity.supportFragmentManager,PreviewFragment.newInstance(it),true,PreviewFragment.javaClass.simpleName,ANIM_LEFT_RIGHT)

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
            showSelectPhotoView()
        }

        dataBinding.photoRepeat.setOnClickListener {
            showSelectPhotoView()
        }

        dataBinding.photoBtnNext.setOnClickListener {
            viewModel.onNextButtonClickListener()
        }

        dataBinding.mtActionBarIgnore.setOnClickListener {
            viewModel.onIgnoreButtonClickListener()
        }

    }

    private fun showSelectPhotoView(){

        PhotoSelector.instance.showPhotoSelectorView(fragmentActivity) { result ->

            viewModel.onCatchPhotoListener(result, fragmentActivity.contentResolver)

        }
    }



}