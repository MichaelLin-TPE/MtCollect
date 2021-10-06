package com.collect.collectpeak.fragment.mountain.mt_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.databinding.MtListItemLayoutBinding
import com.collect.collectpeak.firebase.MountainData

class MtListAdapter : RecyclerView.Adapter<MtListAdapter.ViewHolder>() {

    private lateinit var mtList : ArrayList<MountainData>

    fun setMtList(mtList : ArrayList<MountainData>){
        this.mtList = mtList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding = MtListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mtList[position])
    }

    override fun getItemCount(): Int = mtList.size

    class ViewHolder(private val dataBinding:  MtListItemLayoutBinding) : RecyclerView.ViewHolder(dataBinding.root) {


        fun bind(mountainData: MountainData) {

            val viewModel = MtListViewModel(dataBinding,mountainData)
            dataBinding.vm = viewModel
            dataBinding.executePendingBindings()

        }


    }

}