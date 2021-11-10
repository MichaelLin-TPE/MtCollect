package com.collect.collectpeak.fragment.member.page_fragment.goal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.collect.collectpeak.MtCollectorFragment
import com.collect.collectpeak.R
import com.collect.collectpeak.activity.GoalActivity
import com.collect.collectpeak.databinding.FragmentGoalBinding
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool


class GoalFragment : MtCollectorFragment() {

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding : FragmentGoalBinding

    private var uid = ""

    private val viewModel : GoalViewModel by activityViewModels {
        GoalViewModel.GoalFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(uid: String) =
            GoalFragment().apply {
                arguments = Bundle().apply {
                    putString("uid",uid)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString("uid","")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume(uid)
        observerHandle()
    }

    private fun observerHandle() {

        viewModel.summitArrayLiveData.observe(this,{

            MichaelLog.i("顯示登頂資料數量 : ${it.size}")

            val adapter = GoalAdapter()

            adapter.setData(it)

            dataBinding.goalRecyclerView.adapter = adapter

            adapter.setOnGoalPhotoClickListener(object : GoalAdapter.OnGoalPhotoClickListener{
                override fun onClick(data: SummitData) {

                    goToGoalPage(data)

                }
            })

        })

    }

    private fun goToGoalPage(data: SummitData) {
        val intent = Intent(fragmentActivity,GoalActivity::class.java)
        intent.putExtra("data",data)
        intent.putExtra("uid",uid)
        fragmentActivity.startActivity(intent)
        Tool.startActivityInAnim(fragmentActivity,2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goal,container,false)

        dataBinding.vm = viewModel

        dataBinding.lifecycleOwner = this

        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun initView(root: View) {

        dataBinding.goalRecyclerView.layoutManager = GridLayoutManager(fragmentActivity,4)

    }


}