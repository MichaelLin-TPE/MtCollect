package com.collect.collectpeak.fragment.mountain.mt_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.databinding.MtListItemLayoutBinding
import com.collect.collectpeak.firebase.MountainData

class MtListAdapter : RecyclerView.Adapter<MtListAdapter.ViewHolder>() {

    private lateinit var mtList : ArrayList<MountainData>

    private lateinit var onMountainFootPrintClickListener: MtListViewModel.OnMountainFootPrintClickListener

    fun setOnMountainFootPrintClickListener(onMountainFootPrintClickListener: MtListViewModel.OnMountainFootPrintClickListener){
        this.onMountainFootPrintClickListener = onMountainFootPrintClickListener
    }

    fun setMtList(mtList : ArrayList<MountainData>){
        this.mtList = mtList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding = MtListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mtList[position])
        holder.setOnMountainFootPrintClickListener(onMountainFootPrintClickListener)

    }

    override fun getItemCount(): Int = mtList.size

    class ViewHolder(private val dataBinding:  MtListItemLayoutBinding) : RecyclerView.ViewHolder(dataBinding.root) {

        private lateinit var onMountainFootPrintClickListener: MtListViewModel.OnMountainFootPrintClickListener

        fun setOnMountainFootPrintClickListener(onMountainFootPrintClickListener: MtListViewModel.OnMountainFootPrintClickListener){
            this.onMountainFootPrintClickListener = onMountainFootPrintClickListener
        }

        fun bind(mountainData: MountainData) {

            val viewModel = MtListViewModel(dataBinding,mountainData)
            dataBinding.vm = viewModel
            dataBinding.executePendingBindings()

            viewModel.setOnMountainFootPrintClickListener(object : MtListViewModel.OnMountainFootPrintClickListener{
                override fun onMtFootPrintClick(data: MountainData) {
                    onMountainFootPrintClickListener.onMtFootPrintClick(data)
                }
            })

        }


    }

}