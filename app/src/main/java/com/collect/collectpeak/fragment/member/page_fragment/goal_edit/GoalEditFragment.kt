package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

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
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.FragmentGoalEditBinding
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool


class GoalEditFragment : Fragment() {

    private lateinit var targetSummitData: SummitData

    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var dataBinding : FragmentGoalEditBinding

    private val viewModel : GoalEditViewModel by activityViewModels {
        GoalEditViewModel.GoalEditFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
    }


    companion object {

        @JvmStatic
        fun newInstance(summitData: SummitData) =
            GoalEditFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable("data",summitData)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetSummitData = it.getParcelable<SummitData>("data") as SummitData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goal_edit,container,false)

        dataBinding.vm = viewModel

        dataBinding.lifecycleOwner = this

        initView(dataBinding.root)

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onFragmentStart(targetSummitData)

        observerHandle()
    }

    private fun observerHandle() {
        viewModel.mtPhotoListLivData.observe(this,{

            val adapter = GoalEditPhotoAdapter()

            adapter.setPhotoArray(it)

            dataBinding.editRecyclerView.adapter = adapter


        })
    }

    private fun initView(view: View) {

        val linearLayoutManager = LinearLayoutManager(fragmentActivity)

        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        dataBinding.editRecyclerView.layoutManager = linearLayoutManager

        dataBinding.btnBack.setOnClickListener {

            MichaelLog.i("關閉編輯頁面")
            fragmentActivity.finish()
            fragmentActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }
    }


}