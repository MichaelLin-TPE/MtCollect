package com.collect.collectpeak.fragment.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentProfileBinding
import com.collect.collectpeak.tool.Tool
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : MtCollectorFragment() {

    private lateinit var dataBinding: FragmentProfileBinding

    private lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    private val viewModel: ProfileViewModel by activityViewModels {
        val repository = ProfileRepositoryImpl()
        ProfileViewModel.ProfileFactory(repository)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart()
        handleObserver()
    }

    private fun handleObserver() {
        viewModel.memberDataLiveData.observe(this, { data ->
            dataBinding.profileEditEmail.setText(data.email)

            dataBinding.profileEditEmail.isEnabled = false

            dataBinding.profileEditName.setText(data.name)

            dataBinding.profileEditDescription.setText(data.description)

            dataBinding.profileEditTime.setText(
                SimpleDateFormat(
                    "yyyy/MM/dd",
                    Locale.getDefault()
                ).format(Date(data.time))
            )

            dataBinding.profileEditTime.isEnabled = false
        })

        viewModel.toastLiveData.observe(this,{

            if (it.isEmpty()){
                return@observe
            }

            Toast.makeText(fragmentActivity,it,Toast.LENGTH_LONG).show()
        })

        viewModel.progressDialogLiveData.observe(this,{

            if (!it){
                return@observe
            }

            showProgressDialog(fragmentActivity.supportFragmentManager,"編輯中")
        })

        viewModel.dismissDialogLIveData.observe(this,{
            if (!it){
                return@observe
            }
            dismissProgressDialog()
        })

        viewModel.finishPageLiveData.observe(this,{

            if (!it){
                return@observe
            }
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)

        })


    }

    override fun onPause() {
        super.onPause()
        viewModel.toastLiveData.value = ""
        viewModel.toastLiveData.removeObservers(this)

        viewModel.progressDialogLiveData.value = false
        viewModel.progressDialogLiveData.removeObservers(this)

        viewModel.dismissDialogLIveData.value = false
        viewModel.dismissDialogLIveData.removeObservers(this)

        viewModel.finishPageLiveData.value = false
        viewModel.finishPageLiveData.removeObservers(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {
        dataBinding.profileBack.setOnClickListener {
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity, 2)
        }

        dataBinding.profileDone.setOnClickListener {

            val name = dataBinding.profileEditName.text.toString()

            val description = dataBinding.profileEditDescription.text.toString()

            viewModel.onButtonDoneClickListener(name,description)
        }

    }


}