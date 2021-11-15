package com.collect.collectpeak.fragment.message

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
import com.collect.collectpeak.databinding.FragmentMessageBinding
import com.collect.collectpeak.tool.ButtonClickHandler


class MessageFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentMessageBinding

    private val adapter = MessageAdapter()

    private val viewModel : MessageViewModel by activityViewModels {
        MessageViewModel.MessageFactory()
    }

    private lateinit var fragmentActivity : FragmentActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            MessageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart()
        observerHandle()
    }

    private fun observerHandle() {

        viewModel.updateMessageListLiveData.observe(this,{
            adapter.setData(it);
            adapter.notifyDataSetChanged()
        })

        viewModel.showMessageListLiveData.observe(this,{

            adapter.setData(it)
            dataBinding.messageRecyclerView.adapter = adapter

            adapter.setOnMessageListItemClickListener{

            }
        })
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

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_message,container,false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.messageRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        viewModel.setOnMessageClickEventListener(object : OnMessageClickEventListener{
            override fun onBackPress() {
                fragmentActivity.finish()
            }

            override fun onShowToast(content: String) {
                showToast(content)
            }
        })

    }


}