package com.collect.collectpeak.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.MountainData
import com.collect.collectpeak.fragment.mountain.peak_time.PeakTimeFragment
import com.collect.collectpeak.fragment.mountain.peak_time.PeakTimeFragment.Companion.NORMAL
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.FragmentUtil.Companion.ANIM_RIGHT_LEFT
import com.collect.collectpeak.tool.StatusBarTool

class PeakActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peak)

        val bundle = intent.extras

        val data = bundle?.getSerializable("data") as MountainData

        FragmentUtil.replace(R.id.container,supportFragmentManager,PeakTimeFragment.newInstance(data,NORMAL),true,PeakTimeFragment.javaClass.simpleName,ANIM_RIGHT_LEFT)

        StatusBarTool.setStatusBarSameColorAsActionBar(window,findViewById(R.id.container))

    }
}