package com.collect.collectpeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.member.page_fragment.goal_detail.GoalDetailFragment
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.tool.FragmentUtil

class GoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)


        val data = intent.extras?.getParcelable<SummitData>("data") as SummitData

        FragmentUtil.replace(R.id.container,supportFragmentManager,GoalDetailFragment.newInstance(data),false,GoalDetailFragment.javaClass.simpleName,2)

    }
}