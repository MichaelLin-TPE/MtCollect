package com.collect.collectpeak.main_frame

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R

class MainFrameRepositoryImpl : MainFrameRepository {

    private val tabStringArray = ArrayList<String>().apply {
        val context = MtCollectorApplication.getInstance().getContext()
        this.add(context.getString(R.string.home))
        this.add(context.getString(R.string.mountain))
        this.add(context.getString(R.string.equipment))
        this.add(context.getString(R.string.share))
        this.add(context.getString(R.string.member))
    }

    private val tabIconDarkArray = ArrayList<Drawable>().apply {
        val context = MtCollectorApplication.getInstance().getContext()
        this.add(ContextCompat.getDrawable(context, R.drawable.home_not_press)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.mt_not_press)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.equipment_not_press)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.chat_not_press)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.user_not_press)!!)
    }
    private val tabIconLightArray = ArrayList<Drawable>().apply {
        val context = MtCollectorApplication.getInstance().getContext()
        this.add(ContextCompat.getDrawable(context, R.drawable.home_pressed)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.mt_pressed)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.equipment_pressed)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.chat_pressed)!!)
        this.add(ContextCompat.getDrawable(context, R.drawable.user_pressed)!!)
    }


    override fun getTabData(): TabData {
        return TabData().apply {
            this.tabTitleArray = tabStringArray
            this.tabIconNotPressArray = tabIconDarkArray
            this.tabIconPressedArray = tabIconLightArray
        }
    }
}