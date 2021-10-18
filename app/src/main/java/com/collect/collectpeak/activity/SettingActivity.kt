package com.collect.collectpeak.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsControllerCompat
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentFragment
import com.collect.collectpeak.fragment.setting.SettingFragment
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.StatusBarTool

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        FragmentUtil.replace(R.id.container,supportFragmentManager,
            SettingFragment.newInstance(),true,
            SettingFragment.javaClass.simpleName,1)


        StatusBarTool.setStatusBarSameColorAsActionBar(window,findViewById(R.id.container))

    }
}