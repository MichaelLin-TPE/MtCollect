package com.collect.collectpeak.fragment.home.peak_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PeakListAdapter : RecyclerView.Adapter<PeakListAdapter.ViewHolder>() {

    private lateinit var dataArray : ArrayList<SummitData>

    fun setData(dataArray : ArrayList<SummitData>){
        this.dataArray = dataArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.peak_list_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataArray[position]

        holder.tvTitle.text = data.mtName

        holder.tvTime.text = "登頂時間 : "+SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(data.mtTime))
    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView.findViewById<TextView>(R.id.peak_item_title)

        val tvTime = itemView.findViewById<TextView>(R.id.peak_item_time)
    }
}