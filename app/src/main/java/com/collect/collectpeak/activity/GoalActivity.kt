package com.collect.collectpeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.member.page_fragment.goal_detail.GoalDetailFragment
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData

class GoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)


        var data = SummitData()
        var uid = ""

        intent.extras?.let {
            data = it.getParcelable<SummitData>("data") as SummitData
            uid = it.getString("uid","")
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container,GoalDetailFragment.newInstance(data,uid)).commit()

    }
}