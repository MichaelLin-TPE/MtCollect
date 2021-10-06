package com.collect.collectpeak.fragment.home.news

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.databinding.MtNewsLayoutBinding
import kotlin.collections.ArrayList

class NewsViewModel(private val dataBinding: MtNewsLayoutBinding) {

    private lateinit var onNewsItemClickListener: NewsAdapter.OnNewsItemClickListener


    fun setOnNewsItemClickListener(onNewsItemClickListener: NewsAdapter.OnNewsItemClickListener){
        this.onNewsItemClickListener = onNewsItemClickListener
    }


    fun showView(newsList: ArrayList<NewsData>) {

        dataBinding.newsProgress.visibility = if (newsList.isEmpty()) View.VISIBLE else View.GONE

        dataBinding.newsRecyclerView.visibility = if (newsList.isEmpty()) View.GONE else View.VISIBLE


        val adapter = NewsAdapter()

        adapter.setNewsList(newsList)

        dataBinding.newsRecyclerView.layoutManager = LinearLayoutManager(MtCollectorApplication.getInstance().getContext())

        dataBinding.newsRecyclerView.adapter = adapter

        adapter.setOnNewsItemClickListener(object : NewsAdapter.OnNewsItemClickListener{
            override fun onNewsItemClick(url: String) {
               onNewsItemClickListener.onNewsItemClick(url)
            }

        })
    }

}