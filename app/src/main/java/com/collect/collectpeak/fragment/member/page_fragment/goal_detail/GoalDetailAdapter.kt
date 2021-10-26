package com.collect.collectpeak.fragment.member.page_fragment.goal_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.mountain.peak_photo.PhotoAdapter
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GoalDetailAdapter : RecyclerView.Adapter<GoalDetailAdapter.ViewHolder>() {

    private lateinit var dataArray: ArrayList<SummitData>

    private lateinit var onGoalSettingClickListener: OnGoalSettingClickListener

    fun setOnGoalSettingClickListener(onGoalSettingClickListener: OnGoalSettingClickListener){
        MichaelLog.i("init setOnGoalSettingClickListener")
        this.onGoalSettingClickListener = onGoalSettingClickListener
    }

    fun setDataArray(dataArray: ArrayList<SummitData>) {
        this.dataArray = dataArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_detail_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataArray[position]

        holder.tvLevel.text = MtCollectorApplication.getInstance().getContext().getString(R.string.title_level)

        holder.tvTime.text = MtCollectorApplication.getInstance().getContext().getString(R.string.title_time)

        holder.tvDesc.text = MtCollectorApplication.getInstance().getContext().getString(R.string.title_desc)

        holder.tvTitle.text = data.mtName

        holder.tvLevel.append(" "+data.mtLevel)

        holder.tvTime.append(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(data.mtTime)))

        holder.tvDesc.append(data.description)
        holder.viewPager.setBackgroundColor(ContextCompat.getColor(MtCollectorApplication.getInstance().getContext(),R.color.photo_black))


        holder.ivSetting.setOnClickListener {
            onGoalSettingClickListener.onSettingClick(data)
        }

        if (data.photoArray.isEmpty()){
            return
        }

        val adapter = DetailPhotoAdapter()
        adapter.setData(data.photoArray,MtCollectorApplication.getInstance().getContext())
        holder.viewPager.adapter = adapter


    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.detail_title)

        val viewPager: ViewPager = itemView.findViewById(R.id.detail_viewpager)

        val tvLevel: TextView = itemView.findViewById(R.id.detail_level)

        val tvTime: TextView = itemView.findViewById(R.id.detail_time)

        val tvDesc: TextView = itemView.findViewById(R.id.detail_desc)

        val ivSetting : ImageView = itemView.findViewById(R.id.detail_setting)

    }

    interface OnGoalSettingClickListener{
        fun onSettingClick(data : SummitData)
    }

}