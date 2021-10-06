package com.collect.collectpeak.fragment.home.news

import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.databinding.MtNewsLayoutBinding

class NewsViewHolder(private val dataBinding:  MtNewsLayoutBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    private lateinit var onNewsItemClickListener: NewsAdapter.OnNewsItemClickListener

    fun setOnNewsItemClickListener(onNewsItemClickListener: NewsAdapter.OnNewsItemClickListener){
        this.onNewsItemClickListener = onNewsItemClickListener
    }


    fun showView(newsList: ArrayList<NewsData>) {

        val viewModel = NewsViewModel(dataBinding)
        viewModel.showView(newsList)
        dataBinding.vm = viewModel
        dataBinding.executePendingBindings()
        viewModel.setOnNewsItemClickListener(object : NewsAdapter.OnNewsItemClickListener{
            override fun onNewsItemClick(url: String) {
                onNewsItemClickListener.onNewsItemClick(url)
            }

        })

    }




}