package com.collect.collectpeak.fragment.share

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
import com.collect.collectpeak.databinding.FragmentShareBinding


class ShareFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentShareBinding

    private lateinit var fragmentActivity: FragmentActivity

    private val viewModel : ShareViewModel by activityViewModels {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_share,container,false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.shareRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        dataBinding.shareAddIcon.setOnClickListener {

        }
    }


}