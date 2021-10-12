package com.collect.collectpeak.fragment.equipment.equipment_edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentData

class UserEquipmentListAdapter : RecyclerView.Adapter<UserEquipmentListAdapter.ViewHolder>() {

    private lateinit var dataArray : ArrayList<EquipmentData>

    fun setDataArray(dataArray : ArrayList<EquipmentData>){
        this.dataArray = dataArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.equipment_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataArray[position]

        holder.tvName.text = data.name

        holder.tvContent.text = data.description

    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.equipment_item_title)
        val tvContent: TextView = itemView.findViewById(R.id.equipment_item_description)
        val line : View = itemView.findViewById(R.id.line)

        init {
            line.setBackgroundColor(ContextCompat.getColor(MtCollectorApplication.getInstance().getContext(),R.color.page_background))
        }
    }
}