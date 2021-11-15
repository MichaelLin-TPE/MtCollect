package com.collect.collectpeak.fragment.notice

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
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentNoticeBinding
import com.collect.collectpeak.tool.ButtonClickHandler


class NoticeFragment : Fragment() {

    private lateinit var dataBinding : FragmentNoticeBinding

    private val adapter = NoticeAdapter()

    private var isShowed = false

    private val viewModel : NoticeViewModel by activityViewModels {
        NoticeViewModel.NoticeFactory()
    }

    private lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            NoticeFragment().apply {
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

        viewModel.showNotificationListView.observe(this,{

            if (isShowed){
                adapter.setData(it)
                adapter.notifyDataSetChanged()
                dataBinding.noticeRecyclerView.scrollToPosition(it.size - 1)
                return@observe
            }

            adapter.setData(it)

            dataBinding.noticeRecyclerView.adapter = adapter
            isShowed = true
            dataBinding.noticeRecyclerView.scrollToPosition(it.size - 1)

            adapter.setOnNoticeItemClickListener(object : NoticeAdapter.OnNoticeItemClickListener{
                override fun onApplyFriendAccept(data: NoticeData) {
                    viewModel.onApplyFriendAcceptListener(data)
                }

                override fun onApplyFriendReject(data: NoticeData) {
                    viewModel.onApplyFriendRejectListener(data)
                }

            })

        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_notice,container,false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)

        initView(dataBinding.root)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.noticeRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        viewModel.setOnNoticeClickEventListener(object : OnNoticeClickEventListener{
            override fun onBackPress() {
                fragmentActivity.finish()
            }
        })
    }


}