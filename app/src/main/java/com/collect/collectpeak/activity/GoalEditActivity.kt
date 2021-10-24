package com.collect.collectpeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.member.page_fragment.goal_edit.GoalEditFragment
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.Tool

class GoalEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_edit)


        val data = intent.extras?.getParcelable<SummitData>("data") as SummitData

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container,GoalEditFragment.newInstance(data)).commit()

    }

}