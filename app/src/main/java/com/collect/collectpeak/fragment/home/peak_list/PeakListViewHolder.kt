package com.collect.collectpeak.fragment.home.peak_list

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.tool.TypeFaceHelper
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator
import kotlin.collections.ArrayList

class PeakListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val handler = Handler(Looper.myLooper()!!)

    private val tvContent = itemView.findViewById<TextView>(R.id.billboard_content)

    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.billboard_recycler_view)

    fun showView() {
        itemView.findViewById<TextView>(R.id.billboard_title)
        handler.removeCallbacks(firstRunAnimation)
        handler.removeCallbacks(secondRunAnimation)
        handler.removeCallbacks(thirdRunAnimation)
        handler.removeCallbacks(finalRunAnimation)
        handler.postDelayed(firstRunAnimation, 500)
        recyclerView.layoutManager = LinearLayoutManager(MtCollectorApplication.getInstance().getContext())

        FireStoreHandler.getInstance().getUserSummitData(object :
            FireStoreHandler.OnFireStoreCatchDataListener<ArrayList<SummitData>> {
            override fun onCatchDataSuccess(data: ArrayList<SummitData>) {
                handler.removeCallbacks(firstRunAnimation)
                handler.removeCallbacks(secondRunAnimation)
                handler.removeCallbacks(thirdRunAnimation)
                handler.removeCallbacks(finalRunAnimation)
                tvContent.text = ""


                data.sortWith(Comparator { p0, p1 ->

                    if (p0.mtTime > p1.mtTime){
                        return@Comparator -1
                    }
                    return@Comparator 0
                })

                val adapter = PeakListAdapter()

                adapter.setData(data)

                recyclerView.adapter = adapter


            }

            override fun onCatchDataFail() {

            }

        })

    }

    private val firstRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(secondRunAnimation, 500)
    }

    private val secondRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(thirdRunAnimation, 500)
    }

    private val thirdRunAnimation = Runnable {
        tvContent.append(".")
        handler.postDelayed(finalRunAnimation, 500)
    }

    private val finalRunAnimation = Runnable {
        tvContent.text =
            MtCollectorApplication.getInstance().getContext().getString(R.string.loading)
        restartAnimation()
    }

    private fun restartAnimation() {
        handler.postDelayed(firstRunAnimation, 500)
    }
}