package com.collect.collectpeak.fragment.equipment.equipment_select

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import java.util.ArrayList

class EquipmentNewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = EquipmentListData()

    private lateinit var onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener

    fun setOnEquipmentCheckClickListener(onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener){
        this.onEquipmentCheckClickListener = onEquipmentCheckClickListener
    }

    fun setData(data : EquipmentListData){
        this.data = data
        notifyDataSetChanged()
    }

    companion object{
        const val TITLE = 0
        const val LIST = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if (viewType == TITLE){

            val view = LayoutInflater.from(parent.context).inflate(R.layout.equipment_title_layout,parent,false)
            return EquipmentTitleViewHolder(view)

        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.equipment_new_list_layout,parent,false)
        return EquipmentListViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is EquipmentTitleViewHolder){

            holder.bind(data.titleArray[data.indexArray[position]])
            return
        }
        if (holder is EquipmentListViewHolder){
            holder.bind(data.equipmentList[data.indexArray[position]])
            holder.setOnEquipmentCheckClickListener(onEquipmentCheckClickListener)
        }



    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0 || position % 2 == 0) {
            return TITLE
        }

        return LIST
    }

    override fun getItemCount(): Int = data.indexArray.size


    class EquipmentTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvTitle = itemView.findViewById<TextView>(R.id.equipment_list_title)

        fun bind(title: String) {
            tvTitle.text = when(title){
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
        }

    }

    class EquipmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener

        fun setOnEquipmentCheckClickListener(onEquipmentCheckClickListener: EquipmentItemViewModel.OnEquipmentCheckClickListener){
            this.onEquipmentCheckClickListener = onEquipmentCheckClickListener
        }

        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.equipment_list_recycler_view)

        fun bind(dataList: ArrayList<EquipmentData>) {
            val adapter = EquipmentListAdapter()
            adapter.setDataList(dataList)
            recyclerView.layoutManager = LinearLayoutManager(MtCollectorApplication.getInstance().getContext())
            recyclerView.adapter = adapter
            adapter.setOnEquipmentCheckClickListener(object : EquipmentItemViewModel.OnEquipmentCheckClickListener{
                override fun onCheck(data: EquipmentData) {
                    onEquipmentCheckClickListener.onCheck(data)
                }

            })
        }

    }

}


