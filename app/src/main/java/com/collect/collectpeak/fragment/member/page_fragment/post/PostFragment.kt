package com.collect.collectpeak.fragment.member.page_fragment.post

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentPostBinding


class PostFragment : Fragment() {

    private lateinit var dataBinding : FragmentPostBinding

    private val viewModel : PostViewModel by activityViewModels {
        PostViewModel.PostFactory()
    }

    private lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PostFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_post,container,false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {
        dataBinding.postRecyclerView.layoutManager = GridLayoutManager(fragmentActivity,4)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume()
        observerHandle()
    }

    private fun observerHandle() {
        viewModel.showPostListLiveData.observe(this,{

            val adapter = PostAdapter()
            adapter.setData(it)
            dataBinding.postRecyclerView.adapter = adapter

            adapter.setOnPostPhotoClickListener {
                intentToPostEditPage()
            }

        })

    }

    private fun intentToPostEditPage() {

    }

}