package com.collect.collectpeak.fragment.member.page_fragment.goal_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.collect.collectpeak.activity.GoalEditActivity
import com.collect.collectpeak.databinding.FragmentGoalDetailBinding
import com.collect.collectpeak.dialog.ConfirmDialog
import com.collect.collectpeak.dialog.GoalSettingDialog
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.main_frame.OnBackButtonClickEventCallBackListener
import com.collect.collectpeak.tool.ButtonClickHandler
import com.collect.collectpeak.tool.Tool


class GoalDetailFragment : MtCollectorFragment() {

    private lateinit var targetSummitData: SummitData

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding: FragmentGoalDetailBinding

    private val goalSettingDialog = GoalSettingDialog.instance

    private val adapter = GoalDetailAdapter()

    private val viewModel: GoalDetailViewModel by activityViewModels {
        GoalDetailViewModel.GoalDetailFactory()
    }

    companion object {
        @JvmStatic
        fun newInstance(data: SummitData) =
            GoalDetailFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable("data", data)
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
            targetSummitData = it.getParcelable<SummitData>("data") as SummitData
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentStart(targetSummitData)

        observerHandle()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerHandle() {
        viewModel.showToastLiveData.observe(this, {
            if (it.isEmpty()) {
                return@observe
            }
            showToast(it)
        })

        viewModel.showSummitList.observe(this, {

            adapter.setDataArray(it)
            dataBinding.goalDetailRecyclerView.adapter = adapter

            adapter.setOnGoalSettingClickListener(object :
                GoalDetailAdapter.OnGoalSettingClickListener {
                override fun onSettingClick(data: SummitData) {
                    showGoalSettingDialog(data)
                }

            })

        })

        viewModel.scrollPositionLiveData.observe(this, {
            if (it == 0) {
                return@observe
            }

            dataBinding.goalDetailRecyclerView.smoothScrollToPosition(it)
        })

        viewModel.updateSummitList.observe(this, {

            adapter.setDataArray(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.goToEditPageLiveData.observe(this,{
            if (it.mtName.isEmpty()){
                return@observe
            }
            MichaelLog.i("前進編輯頁面")
            val intent = Intent(fragmentActivity,GoalEditActivity::class.java)
            intent.putExtra("data",it)
            fragmentActivity.startActivity(intent)
            Tool.startActivityInAnim(fragmentActivity,1)
        })

        viewModel.finishPageLiveData.observe(this , {
            if (!it){
                return@observe
            }
            fragmentActivity.finish()
            Tool.startActivityOutAnim(fragmentActivity,2)
        })


    }

    private fun showGoalSettingDialog(data: SummitData) {
        goalSettingDialog.showDialog(dataBinding.goalDetailMask, dataBinding.root, fragmentActivity)
        goalSettingDialog.setOnSettingDialogItemClickListener(object :
            GoalSettingDialog.OnSettingDialogItemClickListener {
            override fun onEditClick() {
                viewModel.onSummitDataEditClickListener(data)
            }

            override fun onDeleteClick() {

                showConfirmDialog(fragmentActivity.supportFragmentManager,"確認是否移除此篇?",object : ConfirmDialog.OnConfirmDialogClickListener{
                    override fun onConfirm() {
                        viewModel.onSummitDataDeleteClickListener(data)
                    }

                    override fun onCancel() {

                    }

                })


            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_goal_detail, container, false)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.clickListener = ButtonClickHandler(viewModel)
        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.showToastLiveData.value = ""
        viewModel.scrollPositionLiveData.value = 0
        viewModel.goToEditPageLiveData.value = SummitData()
        viewModel.finishPageLiveData.value = false
        viewModel.finishPageLiveData.removeObservers(this)
        viewModel.goToEditPageLiveData.removeObservers(this)
        viewModel.showToastLiveData.removeObservers(this)
        viewModel.scrollPositionLiveData.removeObservers(this)
    }

    private fun initView(root: View) {
        dataBinding.goalDetailRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity)

        viewModel.setOnBackButtonClickEventCallBackListener {
            fragmentActivity.finish()
        }


    }


}