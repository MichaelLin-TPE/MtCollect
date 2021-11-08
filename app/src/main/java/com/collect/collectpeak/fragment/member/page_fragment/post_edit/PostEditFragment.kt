package com.collect.collectpeak.fragment.member.page_fragment.post_edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentPostEditBinding
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.PhotoSelector


class PostEditFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentPostEditBinding

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var targetShareData : ShareData

    private  val adapter = PostEditPhotoAdapter()

    private val viewModel : PostEditViewModel by activityViewModels {
        PostEditViewModel.PostEditFactory()
    }


    companion object {
        @JvmStatic
        fun newInstance(data: ShareData) =
            PostEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data",data)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetShareData = it.getParcelable<ShareData>("data") as ShareData
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume(targetShareData)
        observerHandle()
    }

    private fun observerHandle() {
        viewModel.photoListLiveData.observe(this,{



            adapter.setData(it)

            dataBinding.postPhotoRecyclerview.adapter = adapter

            adapter.setonPhotoRemoveClickListener(object : PostEditPhotoAdapter.OnPhotoRemoveClickListener{
                override fun onRemove(photoUrl: String) {
                    viewModel.onRemovePhotoByPhotoUrl(photoUrl)
                }

                override fun onRemoveBitmap(position: Int) {
                    viewModel.onRemovePhotoByBitmap(position)
                }
            })

            adapter.setOnAddPhotoClickListener(object : PostEditPhotoAdapter.OnAddPhotoClickListener{
                override fun onAdd() {
                    viewModel.onAddPhotoClickListener()
                }
            })

        })

        viewModel.updatePhotoListLiveData.observe(this,{
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_edit,container,false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(root: View) {
        viewModel.setOnBackButtonClickEventCallBackListener{

            fragmentActivity.supportFragmentManager.popBackStack()

        }
        val manager = LinearLayoutManager(fragmentActivity)
        manager.orientation = LinearLayoutManager.HORIZONTAL

        dataBinding.postPhotoRecyclerview.layoutManager = manager

        dataBinding.postEditDesc.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(desc: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onCatchDescriptionDifferent(desc.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        viewModel.setOnPostEditClickEventListener(object : OnPostEditClickEventListener{
            override fun onShowProgressDialog() {
                showProgressDialog(fragmentActivity.supportFragmentManager,fragmentActivity.getString(R.string.title_editing))
            }

            override fun onShowPictureSelector(picCount : Int) {
                PhotoSelector.instance.showPhotoSelectorView(fragmentActivity, picCount) { result ->

                    viewModel.onCatchPhotoResultListener(result, fragmentActivity.contentResolver)

                }
            }

            override fun onDismissProgressDialog() {
                dismissProgressDialog()
            }

            override fun onShowToast(content: String) {
                showToast(content)
            }

            override fun onFinishPage() {
                fragmentActivity.supportFragmentManager.popBackStack()
            }

        })
    }

}