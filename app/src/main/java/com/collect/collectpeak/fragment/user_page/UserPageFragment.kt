package com.collect.collectpeak.fragment.user_page

import android.content.Context
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
import com.collect.collectpeak.databinding.FragmentUserPageBinding
import com.collect.collectpeak.fragment.chat.ChatFragment
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.FragmentUtil

class UserPageFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentUserPageBinding

    private lateinit var fragmentActivity: FragmentActivity

    private var targetUid = ""

    private val adapter = UserPageAdapter()

    private val viewModel: UserPageViewModel by activityViewModels {
        UserPageViewModel.UserPageFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance(uid: String) =
            UserPageFragment().apply {
                arguments = Bundle().apply {
                    putString("uid", uid)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetUid = it.getString("uid", "")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume(targetUid)
        observerHandle()
    }

    private fun observerHandle() {

        viewModel.showUserPageLiveData.observe(this, {
            MichaelLog.i("顯示UserPage")

            adapter.setUid(targetUid)

            adapter.setFollowButtonData(it.optString("text"), it.optBoolean("enable"))

            adapter.setFragmentManager(fragmentActivity.supportFragmentManager)

            dataBinding.userPageRecyclerView.adapter = adapter

            adapter.setOnMemberInfoClickListener(object : UserPageAdapter.OnUserInfoClickListener {
                override fun onFollowClick() {
                    viewModel.onFollowClickListener(targetUid)
                }

                override fun onMessageClick() {
                    viewModel.onMessageClickListener(targetUid)
                }

            })
        })

        viewModel.updateFollowButtonLiveData.observe(this, {
            adapter.setFollowButtonData(it.optString("text"), it.optBoolean("enable"))
            adapter.notifyDataSetChanged()
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_page, container, false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {
        dataBinding.userPageRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        viewModel.setOnUserPageClickEventListener(object : OnUserPageClickEventListener {

            override fun onBackClick() {
                val activity = activity ?: return
                activity.finish()
            }

            override fun onShowProgressDialog() {
                showProgressDialog(fragmentActivity.supportFragmentManager, "發送中")
            }

            override fun onDismissProgressDialog() {
                dismissProgressDialog()
            }

            override fun onShowToast(content: String) {
                showToast(content)
            }

            override fun onGoToUserChatPage(targetUid: String) {
                FragmentUtil.replace(
                    R.id.container,
                    fragmentActivity.supportFragmentManager,
                    ChatFragment.newInstance(targetUid),
                    true,
                    ChatFragment.javaClass.simpleName,
                    1
                )
            }

        })

    }


}