package com.collect.collectpeak.fragment.mountain.peak_time

import android.app.admin.DevicePolicyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private lateinit var dataArray : ArrayList<CalendarObject>

    private lateinit var onCalendarDateClickListener: CalendarListAdapter.OnCalendarDateClickListener


    fun setOnCalendarDateClickListener(onCalendarDateClickListener: CalendarListAdapter.OnCalendarDateClickListener){
        this.onCalendarDateClickListener = onCalendarDateClickListener
    }

    fun setDataArray(dataArray : ArrayList<CalendarObject>){
        this.dataArray = dataArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showView(dataArray[position])
        holder.setOnCalendarDateClickListener(onCalendarDateClickListener)
    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onCalendarDateClickListener: CalendarListAdapter.OnCalendarDateClickListener

        private val tvMonth = itemView.findViewById<TextView>(R.id.calendar_month)

        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.calendar_recycler_view)

        fun setOnCalendarDateClickListener(onCalendarDateClickListener: CalendarListAdapter.OnCalendarDateClickListener){
            this.onCalendarDateClickListener = onCalendarDateClickListener
        }

        fun showView(calendarObject: CalendarObject) {
            tvMonth.text = "${calendarObject.month}月"

            val adapter = CalendarListAdapter()

            adapter.setDataArray(calendarObject.calendarArray)

            recyclerView.layoutManager = GridLayoutManager(MtCollectorApplication.getInstance().getContext(),7)

            recyclerView.adapter = adapter

            adapter.setOnCalendarDateClickListener(object : CalendarListAdapter.OnCalendarDateClickListener{
                override fun onClick(data: CalendarData) {
                    onCalendarDateClickListener.onClick(data)
                }
            })

        }

    }
}