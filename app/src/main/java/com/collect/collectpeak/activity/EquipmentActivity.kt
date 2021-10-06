package com.collect.collectpeak.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentFragment

class EquipmentActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,EquipmentFragment.newInstance()).commit()

        window.statusBarColor =  Color.TRANSPARENT

        val view = findViewById<FrameLayout>(R.id.container)

        WindowInsetsControllerCompat(window,view).isAppearanceLightStatusBars = true
    }


}