package com.collect.collectpeak.fragment.equipment.equipment_select

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.databinding.EquipmentListLayoutBinding

class EquipmentListViewModel(
    dataBinding: EquipmentListLayoutBinding,
    equipmentObject: EquipmentObject
) {

    private lateinit var onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener

    fun setOnEquipmentCheckClickListener(onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener){
        this.onEquipmentCheckClickListener = onEquipmentCheckClickListener
    }

    val equipmentTitleLiveData = MutableLiveData(
        when(equipmentObject.title){
            "body"->{
                "人身物品"
            }
            "move"->{
                "行動裝備"
            }
            "camp"->{
                "住宿露營"
            }
            "food"->{
                "飲食餐具"
            }
            "elect"->{
                "電子產品"
            }
            "drog"->{
                "衛生藥品"
            }
            else -> {
                "其他配件"
            }
        }
    )

    init {

        dataBinding.equipmentListRecyclerView.layoutManager = LinearLayoutManager(MtCollectorApplication.getInstance().getContext())
        val adapter = EquipmentListAdapter()
        adapter.setOnEquipmentCheckClickListener(object : EquipmentItemViewModel.OnEquipmentCheckClickListener{
            override fun onCheck(data: EquipmentData) {
                onEquipmentCheckClickListener.onCheck(data)
            }

        })

        adapter.setDataList(equipmentObject.equipmentData)
        dataBinding.equipmentListRecyclerView.adapter = adapter

    }

}