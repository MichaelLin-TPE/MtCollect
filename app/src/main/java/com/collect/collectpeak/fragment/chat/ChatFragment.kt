package com.collect.collectpeak.fragment.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentChatBinding
import com.collect.collectpeak.tool.ButtonClickHandler


class ChatFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentChatBinding
    private var targetChatId = ""

    private val viewModel: ChatViewModel by activityViewModels {
        ChatViewModel.ChatFactory()
    }

    private lateinit var fragmentActivity: FragmentActivity


    companion object {
        @JvmStatic
        fun newInstance(targetChatId: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString("chatId",targetChatId)
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
            targetChatId = it.getString("chatId","")
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart(targetChatId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)
        return dataBinding.root
    }

    private fun initView(root: View) {
        viewModel.setOnChatClickEventListener(object : OnChatClickEventListener {
            override fun onBackPress() {
                fragmentActivity.supportFragmentManager.popBackStack()
            }

            override fun onShowToast(content: String) {
                showToast(content)
            }

        })
    }


}