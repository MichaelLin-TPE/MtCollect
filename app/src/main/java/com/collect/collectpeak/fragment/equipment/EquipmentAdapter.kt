package com.collect.collectpeak.fragment.equipment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData

class EquipmentAdapter : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {


    private lateinit var dataArray : ArrayList<EquipmentUserData>

    private lateinit var onEquipmentItemClickListener: OnEquipmentItemClickListener

    fun setOnEquipmentItemClickListener(onEquipmentItemClickListener: OnEquipmentItemClickListener){
        this.onEquipmentItemClickListener = onEquipmentItemClickListener
    }

    fun setDataArray(dataArray : ArrayList<EquipmentUserData>){
        this.dataArray = dataArray
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.equipment_list_item_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(dataArray[position])

        holder.setOnEquipmentItemClickListener(onEquipmentItemClickListener)

    }

    override fun getItemCount(): Int = dataArray.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onEquipmentItemClickListener: OnEquipmentItemClickListener

        fun bind(equipmentUserData: EquipmentUserData) {
            tvName.text = equipmentUserData.name

            tvInfo.text = "需要準備裝備輸量：${equipmentUserData.selectTargetArray.size} 樣"

            rootView.setOnClickListener {
                onEquipmentItemClickListener.onClick(equipmentUserData)
            }

        }

        fun setOnEquipmentItemClickListener(onEquipmentItemClickListener: OnEquipmentItemClickListener) {
            this.onEquipmentItemClickListener = onEquipmentItemClickListener
        }

        private val rootView = itemView.findViewById<ConstraintLayout>(R.id.equipment_item_click)

        private val tvName = itemView.findViewById<TextView>(R.id.equipment_item_name)

        private val tvInfo = itemView.findViewById<TextView>(R.id.equipment_item_info)
    }

    interface OnEquipmentItemClickListener{
        fun onClick(data : EquipmentUserData)
    }
}