package com.collect.collectpeak.fragment.member.page_fragment.post_detail

import android.content.Context
import android.os.Bundle
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
import com.collect.collectpeak.databinding.FragmentPostDetailBinding
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.dialog.GoalSettingDialog
import com.collect.collectpeak.fragment.member.page_fragment.post_edit.PostEditFragment
import com.collect.collectpeak.fragment.mountain.mt_list.MtFragment
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.Tool


class PostDetailFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentPostDetailBinding

    private lateinit var targetShareData: ShareData

    private val adapter = PostAdapter()

    private val viewModel: PostDetailViewModel by activityViewModels {
        PostDetailViewModel.PostDetailFactory()
    }

    private lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance(data: ShareData) =
            PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data", data)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetShareData = it.getParcelable<ShareData>("data") as ShareData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.postDetailRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        viewModel.setOnBackButtonClickEventCallBackListener {
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity, 2)
        }

        //處理點擊事件的 interface
        viewModel.setOnPostDetailClickEventListener(object : OnPostDetailClickEventListener {
            override fun onClickSetting(shareData: ShareData) {
                showSettingDialog(shareData)
            }

            override fun onShowConfirmDeleteDialog(shareData: ShareData) {
                showConfirmDialog(
                    fragmentActivity.supportFragmentManager,
                    "確定要移除此筆貼文?",
                    object : ConfirmDialog.OnConfirmDialogClickListener {
                        override fun onConfirm() {
                            viewModel.onDeletePostConfirmListener(shareData)
                        }

                        override fun onCancel() {

                        }

                    })
            }

            override fun onShowProgressDialog() {
                showProgressDialog(fragmentActivity.supportFragmentManager, "刪除中")
            }

            override fun onDismissProgressDialog() {
                dismissProgressDialog()
            }

            override fun onGotoPostEditPage(shareData: ShareData) {
                FragmentUtil.replace(
                    R.id.container,
                    fragmentActivity.supportFragmentManager,
                    PostEditFragment.newInstance(shareData),
                    true,
                    PostEditFragment.javaClass.simpleName,
                    1
                )
            }

        })

    }

    private fun showSettingDialog(shareData: ShareData) {
        val dialog = GoalSettingDialog.instance
        dialog.showDialog(dataBinding.postDetailMask, dataBinding.root, fragmentActivity)
        dialog.setOnSettingDialogItemClickListener(object :
            GoalSettingDialog.OnSettingDialogItemClickListener {
            override fun onEditClick() {
                viewModel.onEditPostDataClickListener(shareData)
            }

            override fun onDeleteClick() {
                viewModel.onDeletePostClickListener(shareData)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume(targetShareData)
        observerHandle()
    }

    private fun observerHandle() {
        viewModel.showPostListLiveData.observe(this, {
            MichaelLog.i("顯示貼文")

            adapter.setData(it)

            dataBinding.postDetailRecyclerView.adapter = adapter

            adapter.setOnPostDetailClickListener(object : PostAdapter.OnPostDetailClickListener {
                override fun onSettingClick(shareData: ShareData) {
                    viewModel.onSettingClickListener(shareData)
                }

                override fun onHeartIconClick(shareData: ShareData) {
                    viewModel.onHeartIconClickListener(shareData)
                }
            })
        })

        viewModel.scrollToListPositionLiveData.observe(this,{
            dataBinding.postDetailRecyclerView.scrollToPosition(it)
        })

        viewModel.updatePostListLiveData.observe(this, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }


}