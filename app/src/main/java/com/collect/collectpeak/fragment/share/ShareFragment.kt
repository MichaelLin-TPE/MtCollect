package com.collect.collectpeak.fragment.share

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.UserPageActivity
import com.collect.collectpeak.databinding.FragmentShareBinding
import com.collect.collectpeak.fragment.member.page_fragment.post_detail.PostListAdapter
import com.collect.collectpeak.fragment.user_page.UserPageFragment
import com.collect.collectpeak.main_frame.MainFrameActivity
import com.collect.collectpeak.main_frame.MainFrameActivity.Companion.GO_SELF_PAGE
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.Tool


class ShareFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentShareBinding

    private lateinit var fragmentActivity: FragmentActivity

    private val adapter = PostListAdapter()

    private val viewModel: ShareViewModel by activityViewModels {
        val repository = ShareRepositoryImpl()
        ShareViewModel.ShareViewModelFactory(repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {

        const val PEAK_DATA = 0
        const val NORMAL_POST = 1

        @JvmStatic
        fun newInstance() = ShareFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume()
        observerHandle()
    }

    private fun observerHandle() {
        viewModel.postListLiveData.observe(this, {
            adapter.setData(it)

            dataBinding.shareRecyclerView.adapter = adapter
            adapter.setOnPostDetailClickListener(object :
                PostListAdapter.OnPostDetailClickListener {
                override fun onSettingClick(shareData: ShareData) {

                }

                override fun onHeartIconClick(shareData: ShareData) {
                    viewModel.onHeartIconClick(shareData)
                }

                override fun onPhotoClick(uid: String) {
                    viewModel.onUserPhotoCLickListener(uid)
                }
            })
        })

        viewModel.updatePostListLiveData.observe(this, {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_share, container, false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.shareRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        dataBinding.shareAddIcon.setOnClickListener {

        }

        viewModel.setOnShareClickEventListener(object : OnShareClickEventListener {
            override fun onGoToSelfPage() {
                val intent = Intent(fragmentActivity, MainFrameActivity::class.java)
                intent.putExtra("type", GO_SELF_PAGE)
                fragmentActivity.startActivity(intent)
            }

            override fun onGoToUserPage(uid: String) {

                val intent = Intent(fragmentActivity,UserPageActivity::class.java)
                intent.putExtra("uid",uid)
                fragmentActivity.startActivity(intent)
                Tool.startActivityInAnim(fragmentActivity,1)
            }

        })

    }


}