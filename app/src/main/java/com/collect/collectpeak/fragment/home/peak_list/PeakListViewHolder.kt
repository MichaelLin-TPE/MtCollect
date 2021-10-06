package com.collect.collectpeak.fragment.home.peak_list

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.tool.TypeFaceHelper

class PeakListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val handler = Handler(Looper.myLooper()!!)

    private val tvContent = itemView.findViewById<TextView>(R.id.billboard_content)

    fun showView() {
        itemView.findViewById<TextView>(R.id.billboard_title)
        handler.removeCallbacks(firstRunAnimation)
        handler.removeCallbacks(secondRunAnimation)
        handler.removeCallbacks(thirdRunAnimation)
        handler.removeCallbacks(finalRunAnimation)
        handler.postDelayed(firstRunAnimation,500)

    }

    private val firstRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(secondRunAnimation,500)
    }

    private val secondRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(thirdRunAnimation,500)
    }

    private val thirdRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(finalRunAnimation,500)
    }

    private val finalRunAnimation = Runnable {
        tvContent.text = MtCollectorApplication.getInstance().getContext().getString(R.string.loading)
        restartAnimation()
    }

    private fun restartAnimation() {
        handler.postDelayed(firstRunAnimation,500)
    }
}