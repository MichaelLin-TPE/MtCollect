package com.collect.collectpeak.fragment.member.page_fragment.post_detail

import android.content.Context
import android.os.Bundle
import android.os.SharedMemory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentPostDetailBinding
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.Tool


class PostDetailFragment : Fragment() {

    private lateinit var dataBinding : FragmentPostDetailBinding

    private lateinit var targetShareData: ShareData

    private val viewModel : PostDetailViewModel by activityViewModels {
        PostDetailViewModel.PostDetailFactory()
    }

    private lateinit var fragmentActivity: FragmentActivity

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_detail,container,false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {

        viewModel.setOnBackButtonClickEventCallBackListener{
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume(targetShareData)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(data:ShareData) =
            PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data",data)
                }
            }
    }
}