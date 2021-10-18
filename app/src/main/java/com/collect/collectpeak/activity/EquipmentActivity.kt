package com.collect.collectpeak.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsControllerCompat
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.equipment.equipment_edit.EquipmentEditFragment
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentFragment
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.StatusBarTool

class EquipmentActivity : AppCompatActivity() {

    companion object{
        const val SELECT = 0
        const val EDIT = 1
        const val EDIT_LIST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)


        val bundle = intent.extras

        val type : Int? = bundle?.getInt("type",0)

        if (type == SELECT){

            FragmentUtil.replace(R.id.container,supportFragmentManager,EquipmentFragment.newInstance(SELECT),true,EquipmentFragment.javaClass.simpleName,1)

        }else{

            val data = bundle?.getParcelable<EquipmentUserData>("data")

            MichaelLog.i("有收到資料：${data?.name}")

            data?.let { EquipmentEditFragment.newInstance(it) }?.let {
                FragmentUtil.replace(R.id.container,supportFragmentManager,it,true,EquipmentEditFragment.javaClass.simpleName,1)
            }
        }

        StatusBarTool.setStatusBarSameColorAsActionBar(window,findViewById(R.id.container))

    }


}