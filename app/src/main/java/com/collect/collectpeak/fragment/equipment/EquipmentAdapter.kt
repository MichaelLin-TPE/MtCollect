package com.collect.collectpeak.fragment.equipment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.equipment.EquipmentListFragment.Companion.NORMAL
import com.collect.collectpeak.fragment.equipment.equipment_select.EquipmentUserData
import com.collect.collectpeak.log.MichaelLog

class EquipmentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private lateinit var dataArray: ArrayList<EquipmentUserData>

    private lateinit var onEquipmentItemClickListener: OnEquipmentItemClickListener

    private lateinit var onEquipmentDeleteClickListener: OnEquipmentDeleteClickListener

    private var type = 0

    fun setOnEquipmentDeleteClickListener(onEquipmentDeleteClickListener: OnEquipmentDeleteClickListener) {
        this.onEquipmentDeleteClickListener = onEquipmentDeleteClickListener
    }

    fun setOnEquipmentItemClickListener(onEquipmentItemClickListener: OnEquipmentItemClickListener) {
        this.onEquipmentItemClickListener = onEquipmentItemClickListener
    }

    fun setDataArray(dataArray: ArrayList<EquipmentUserData>) {
        this.dataArray = dataArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.equipment_list_item_layout, parent, false)

        if (type == NORMAL) {

            return ViewHolder(view)
        }


        return DeleteViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(dataArray[position])

            holder.setOnEquipmentItemClickListener(onEquipmentItemClickListener)
            return
        }

        if (holder is DeleteViewHolder) {

            holder.bind(dataArray[position])
            holder.setOnEquipmentDeleteClickListener(onEquipmentDeleteClickListener)

        }
    }


    override fun getItemCount(): Int = dataArray.size

    override fun getItemViewType(position: Int): Int {
        return type
    }

    fun setType(type: Int) {
        this.type = type
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onEquipmentItemClickListener: OnEquipmentItemClickListener

        fun bind(equipmentUserData: EquipmentUserData) {
            tvName.text = equipmentUserData.name

            tvInfo.text = "需要準備裝備輸量：${equipmentUserData.selectTargetArray.size} 樣"

            rootView.setOnClickListener {
                onEquipmentItemClickListener.onClick(equipmentUserData)
            }

            rootView.setOnLongClickListener {

                onEquipmentItemClickListener.onLongPress()

                true
            }

        }

        fun setOnEquipmentItemClickListener(onEquipmentItemClickListener: OnEquipmentItemClickListener) {
            this.onEquipmentItemClickListener = onEquipmentItemClickListener
        }

        private val rootView = itemView.findViewById<ConstraintLayout>(R.id.equipment_item_click)

        private val tvName = itemView.findViewById<TextView>(R.id.equipment_item_name)

        private val tvInfo = itemView.findViewById<TextView>(R.id.equipment_item_info)
    }

    class DeleteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var onEquipmentDeleteClickListener: OnEquipmentDeleteClickListener

        fun setOnEquipmentDeleteClickListener(onEquipmentDeleteClickListener: OnEquipmentDeleteClickListener) {
            this.onEquipmentDeleteClickListener = onEquipmentDeleteClickListener
        }

        fun bind(equipmentUserData: EquipmentUserData) {
            tvName.text = equipmentUserData.name

            tvInfo.text = "需要準備裝備輸量：${equipmentUserData.selectTargetArray.size} 樣"

            checkBox.visibility = View.VISIBLE

            checkBox.isChecked = equipmentUserData.isDeleteCheck

            checkBox.setOnClickListener {
                equipmentUserData.isDeleteCheck = !equipmentUserData.isDeleteCheck
                onEquipmentDeleteClickListener.onDelete(equipmentUserData)
            }


            rootView.setOnClickListener {
                equipmentUserData.isDeleteCheck = !equipmentUserData.isDeleteCheck
                onEquipmentDeleteClickListener.onDelete(equipmentUserData)
            }


        }


        private val rootView = itemView.findViewById<ConstraintLayout>(R.id.equipment_item_click)

        private val tvName = itemView.findViewById<TextView>(R.id.equipment_item_name)

        private val tvInfo = itemView.findViewById<TextView>(R.id.equipment_item_info)

        private val checkBox =
            itemView.findViewById<AppCompatCheckBox>(R.id.equipment_item_delete_check)
    }

    interface OnEquipmentDeleteClickListener {
        fun onDelete(data: EquipmentUserData)
    }

    interface OnEquipmentItemClickListener {
        fun onClick(data: EquipmentUserData)

        fun onLongPress()
    }


}