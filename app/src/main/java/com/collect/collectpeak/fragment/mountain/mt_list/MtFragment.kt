package com.collect.collectpeak.fragment.mountain.mt_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.PeakActivity
import com.collect.collectpeak.databinding.FragmentMtBinding
import com.collect.collectpeak.dialog.LevelDialog
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool

class MtFragment : MtCollectorFragment() {

    private lateinit var dataBinding : FragmentMtBinding

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var adapter: MtListAdapter

    private val viewModel : MtViewModel by activityViewModels {
        val mtRepository = MtRepositoryImpl()
        MtViewModel.MtViewHolderFactory(mtRepository)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MtFragment().apply {
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

        observersHandle()


    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentStart()
    }

    //所有的觀察者
    private fun observersHandle() {

        viewModel.mtListLiveData.observe(this,{ mtListData ->
            adapter = MtListAdapter()
            adapter.setMtList(mtListData)
            dataBinding.mtRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)
            dataBinding.mtRecyclerView.adapter = adapter

            adapter.setOnMountainFootPrintClickListener(object : MtListViewModel.OnMountainFootPrintClickListener{
                override fun onMtFootPrintClick(data: MountainData) {
                    MichaelLog.i("點擊 FootPrint : ${data.name}")

                    intentToPeakTimePage(data)

                }
            })

        })

        viewModel.levelDialogLiveData.observe(this, {

            if (it.isNullOrEmpty()){
                return@observe
            }

            val levelDialog = LevelDialog.newInstance()
            levelDialog.setLevelArray(it)
            levelDialog.show(fragmentActivity.supportFragmentManager,"dialog")
            levelDialog.setOnLevelButtonClickListener(object : LevelDialog.OnLevelButtonClickListener{
                override fun onClick(level: String) {
                    MichaelLog.i("點擊了分級：$level")
                    levelDialog.dismiss()
                    viewModel.onLevelButtonClickListener(level)
                }
            })

        })

        viewModel.mtListUpdateLiveData.observe(this,{
            adapter.setMtList(it)
            adapter.notifyDataSetChanged()
        })


    }

    private fun intentToPeakTimePage(data: MountainData) {

        if (!AuthHandler.isLogin()){
            showToast(fragmentActivity.getString(R.string.title_please_login_first))
            return
        }

        val intent = Intent(fragmentActivity,PeakActivity::class.java)

        intent.putExtra("data",data)

        fragmentActivity.startActivity(intent)

        Tool.startActivityInAnim(fragmentActivity,2)
    }

    override fun onPause() {
        super.onPause()
        viewModel.levelDialogLiveData.value = null
        viewModel.levelDialogLiveData.removeObservers(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(fragmentActivity),R.layout.fragment_mt,container,false)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        initView()
        return dataBinding.root
    }

    private fun initView() {
        dataBinding.mtSpinnerView.setOnClickListener {
            viewModel.onLevelSpinnerClickListener()
        }
    }


}