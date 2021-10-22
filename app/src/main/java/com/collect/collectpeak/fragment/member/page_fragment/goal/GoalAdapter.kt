package com.collect.collectpeak.fragment.member.page_fragment.goal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.mountain.peak_preview.SummitData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ImageLoaderHandler
import com.collect.collectpeak.tool.Tool

class GoalAdapter : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {


    private lateinit var dataArray : ArrayList<SummitData>

    fun setData(dataArray : ArrayList<SummitData>){
        this.dataArray = dataArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_item_layout,parent,false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataArray[position]

        if (data.photoArray.isEmpty()){
            return
        }

        ImageLoaderHandler.getInstance().setPhotoUrl(data.photoArray[0],holder.ivPhoto)

    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val root: ConstraintLayout = itemView.findViewById(R.id.goal_root)

        val ivPhoto: ImageView = itemView.findViewById(R.id.goal_photo)

        init {
            val layoutParams = root.layoutParams
            val screenWidth = Tool.getScreenWidth()
            layoutParams.width = screenWidth / 4
            layoutParams.height = screenWidth / 4
            MichaelLog.i("邊框 : ${screenWidth / 4}")
            root.layoutParams = layoutParams

        }


    }
}