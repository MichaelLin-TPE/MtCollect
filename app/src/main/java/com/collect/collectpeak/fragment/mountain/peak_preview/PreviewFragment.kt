package com.collect.collectpeak.fragment.mountain.peak_preview

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentPreviewBinding
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.fragment.mountain.peak_photo.PhotoAdapter
import com.collect.collectpeak.fragment.mountain.peak_time.MtPeakData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool


class PreviewFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var mtPeakData: MtPeakData

    private lateinit var dataBinding : FragmentPreviewBinding

    private val viewModel : PreviewViewModel by activityViewModels {
        PreviewViewModel.PreviewFactory()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {

        @JvmStatic
        fun newInstance(mtPeakData: MtPeakData) =
            PreviewFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable("data",mtPeakData)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.mtPeakData = it.getParcelable<MtPeakData>("data") as MtPeakData
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart(mtPeakData)

        observerHandle()
    }

    private fun observerHandle() {
        viewModel.mtNameLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            dataBinding.previewMtName.append(it)
        })

        viewModel.mtLevelLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            dataBinding.previewMtLevel.append(it)
        })

        viewModel.mtTimeLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            dataBinding.previewMtTime.append(it)
        })

        viewModel.mtPhotoArrayLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            val adapter = PhotoAdapter()
            adapter.setData(it,fragmentActivity)
            dataBinding.previewViewPager.adapter = adapter
            dataBinding.previewViewPager.setBackgroundColor(ContextCompat.getColor(fragmentActivity,R.color.photo_black))

        })

        viewModel.showConfirmDialogLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            showConfirmDialog(fragmentActivity.supportFragmentManager,it,object : ConfirmDialog.OnConfirmDialogClickListener{
                override fun onConfirm() {
                    MichaelLog.i("點擊確定")
                    viewModel.onConfirmShareClickListener()

                }

                override fun onCancel() {
                    MichaelLog.i("點擊取消")
                    viewModel.onCancelShareClickListener()
                }

            })
        })

        viewModel.showProgressDialogLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            showProgressDialog(fragmentActivity.supportFragmentManager,it)

        })

        viewModel.dismissProgressDialogLiveData.observe(this,{
            if (!it){
                return@observe
            }
            dismissProgressDialog()
        })

        viewModel.showToastLiveData.observe(this,{
            if (it.isEmpty()){
                return@observe
            }
            showToast(it)
        })

        viewModel.finishPageLiveData.observe(this,{
            if (!it){
                return@observe
            }
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.mtNameLiveData.value = ""
        viewModel.mtTimeLiveData.value = ""
        viewModel.mtLevelLiveData.value = ""
        viewModel.mtPhotoArrayLiveData.value = ArrayList()
        viewModel.showConfirmDialogLiveData.value = ""
        viewModel.showProgressDialogLiveData.value = ""
        viewModel.dismissProgressDialogLiveData.value = false
        viewModel.showToastLiveData.value = ""
        viewModel.finishPageLiveData.value = false

        viewModel.onFragmentPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_preview,container,false)

        dataBinding.vm = viewModel

        dataBinding.lifecycleOwner = this

        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {
        dataBinding.btnBack.setOnClickListener {
            fragmentActivity.supportFragmentManager.popBackStack()
        }


        dataBinding.mtActionBarDone.setOnClickListener {

            val description = dataBinding.previewDesc.text.toString()


            viewModel.onDoneButtonClickListener(description)
        }
    }


}