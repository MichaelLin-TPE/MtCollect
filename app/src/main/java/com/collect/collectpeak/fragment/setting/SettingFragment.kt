package com.collect.collectpeak.fragment.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentSettingBinding
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.tool.Tool


class SettingFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity


    private lateinit var dataBinding : FragmentSettingBinding

    private val viewModel : SettingViewModel by activityViewModels {
        SettingViewModel.SettingViewModelFactory()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingFragment().apply {
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
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false)

        dataBinding.vm = viewModel

        dataBinding.lifecycleOwner = this

        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        handleObserver()
    }

    private fun handleObserver() {
        viewModel.finishPageLiveData.observe(this,{

            if (!it){
                return@observe
            }

            finishPage()
        })

        viewModel.showLogoutConfirmDialogLiveData.observe(this,{

            if (it.isNullOrEmpty()){
                return@observe
            }
            showConfirmDialog(fragmentActivity.supportFragmentManager,it,object : ConfirmDialog.OnConfirmDialogClickListener{
                override fun onConfirm() {
                    viewModel.onLogoutConfirmClickListener()
                }

                override fun onCancel() {

                }
            })


        })
    }


    override fun onPause() {
        super.onPause()

        viewModel.finishPageLiveData.value = false

        viewModel.showLogoutConfirmDialogLiveData.value = ""

    }

    private fun finishPage() {
        fragmentActivity.finish()
        Tool.startActivityOutAnim(fragmentActivity,2)
    }

    private fun initView(root: View) {

        dataBinding.btnBack.setOnClickListener {
           finishPage()
        }

        dataBinding.settingLogout.setOnClickListener {

            viewModel.onLogoutClickListener()

        }

    }


}