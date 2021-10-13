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

class EquipmentActivity : AppCompatActivity() {

    companion object{
        const val SELECT = 0
        const val EDIT = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        val transaction = supportFragmentManager.beginTransaction()
        val bundle = intent.extras

        val type : Int? = bundle?.getInt("type",0)

        if (type == SELECT){
            transaction.replace(R.id.container,EquipmentFragment.newInstance()).commit()
        }else{

            val data = bundle?.getParcelable<EquipmentUserData>("data")

            MichaelLog.i("有收到資料：${data?.name}")

            data?.let { EquipmentEditFragment.newInstance(it) }?.let { transaction.replace(R.id.container, it).commit() }
        }

        window.statusBarColor =  Color.TRANSPARENT

        val view = findViewById<FrameLayout>(R.id.container)

        WindowInsetsControllerCompat(window,view).isAppearanceLightStatusBars = true
    }


}