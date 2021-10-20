package com.collect.collectpeak.fragment.mountain.peak_time

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R

class CalendarListAdapter : RecyclerView.Adapter<CalendarListAdapter.ViewHolder>() {

    private lateinit var onCalendarDateClickListener: OnCalendarDateClickListener

    private lateinit var dataArray : ArrayList<CalendarData>

    fun setOnCalendarDateClickListener(onCalendarDateClickListener: OnCalendarDateClickListener){
        this.onCalendarDateClickListener = onCalendarDateClickListener
    }

    fun setDataArray(dataArray : ArrayList<CalendarData>){
        this.dataArray = dataArray
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_date_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showView(dataArray[position])
        holder.setOnCalendarDateClickListener(onCalendarDateClickListener)
    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvDate: TextView = itemView.findViewById(R.id.calendar_date)

        private lateinit var onCalendarDateClickListener: OnCalendarDateClickListener

        fun setOnCalendarDateClickListener(onCalendarDateClickListener: OnCalendarDateClickListener){
            this.onCalendarDateClickListener = onCalendarDateClickListener
        }

        fun showView(calendarData: CalendarData) {

            tvDate.text = calendarData.date

            if (calendarData.date.isEmpty()){
                return
            }
            if (calendarData.isCheck){
                tvDate.background = ContextCompat.getDrawable(MtCollectorApplication.getInstance().getContext(),R.drawable.calendar_date_shape)
            }

            tvDate.setOnClickListener {
                onCalendarDateClickListener.onClick(calendarData)
            }

        }

    }


    interface OnCalendarDateClickListener{
        fun onClick(data : CalendarData)
    }

}