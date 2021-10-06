package com.collect.collectpeak.fragment.equipment.equipment_select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.databinding.EquipmentItemLayoutBinding

class EquipmentListAdapter : RecyclerView.Adapter<EquipmentListAdapter.ViewHolder>() {

    private lateinit var dataList: ArrayList<EquipmentData>

    private lateinit var onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener

    fun setOnEquipmentCheckClickListener(onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener){
        this.onEquipmentCheckClickListener = onEquipmentCheckClickListener
    }

    fun setDataList(dataList: ArrayList<EquipmentData>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding =
            EquipmentItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.setOnEquipmentCheckClickListener(onEquipmentCheckClickListener)
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(private val dataBinding: EquipmentItemLayoutBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        private lateinit var onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener

        fun setOnEquipmentCheckClickListener(onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener){
            this.onEquipmentCheckClickListener = onEquipmentCheckClickListener
        }

        fun bind(equipmentData: EquipmentData) {
            val viewModel = EquipmentItemViewModel(equipmentData)
            dataBinding.vm = viewModel
            dataBinding.executePendingBindings()

            dataBinding.equipmentItemRoot.setOnClickListener {
                equipmentData.isCheck = !equipmentData.isCheck
                onEquipmentCheckClickListener.onCheck(equipmentData)
            }

            dataBinding.equipmentItemCheck.setOnCheckedChangeListener { _, isChecked ->
                equipmentData.isCheck = isChecked
                onEquipmentCheckClickListener.onCheck(equipmentData)
            }

        }


    }
}