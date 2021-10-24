package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.tool.ImageLoaderHandler

class GoalEditPhotoAdapter : RecyclerView.Adapter<GoalEditPhotoAdapter.ViewHolder>() {

    private lateinit var photoArray : ArrayList<String>

    fun setPhotoArray(photoArray :ArrayList<String>){
        this.photoArray = photoArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_edit_photo_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = photoArray[position]

        ImageLoaderHandler.getInstance().setPhotoUrl(url,holder.ivPhoto)

        holder.ivClose.setOnClickListener {

        }

    }

    override fun getItemCount(): Int = photoArray.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivPhoto : ImageView = itemView.findViewById(R.id.goal_edit_photo)

        val ivClose : ImageView = itemView.findViewById(R.id.goal_edit_close)

    }
}