package com.collect.collectpeak.fragment.home.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import org.w3c.dom.Text

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var newsList : ArrayList<NewsData>

    private lateinit var onNewsItemClickListener: OnNewsItemClickListener

    fun setOnNewsItemClickListener(onNewsItemClickListener: OnNewsItemClickListener){
        this.onNewsItemClickListener = onNewsItemClickListener
    }

    fun setNewsList(newsList : ArrayList<NewsData>){
        this.newsList = newsList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
        holder.setOnNewsItemClickListener(onNewsItemClickListener)
    }

    override fun getItemCount(): Int = newsList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var tvTitle : TextView

        private lateinit var tvContent : TextView

        private lateinit var root : ConstraintLayout

        private lateinit var onNewsItemClickListener: OnNewsItemClickListener

        @SuppressLint("SetTextI18n")
        fun bind(newsData: NewsData) {

            tvTitle = itemView.findViewById(R.id.news_item_title)
            tvContent = itemView.findViewById(R.id.news_item_content)
            root = itemView.findViewById(R.id.news_root)

            tvTitle.text = newsData.title

            val content = "發布單位：${newsData.location}  發布日期：${newsData.time}"

            tvContent.text = content

            root.setOnClickListener {
                onNewsItemClickListener.onNewsItemClick(newsData.url)
            }

        }

        fun setOnNewsItemClickListener(onNewsItemClickListener: OnNewsItemClickListener) {
            this.onNewsItemClickListener = onNewsItemClickListener
        }

    }

    interface OnNewsItemClickListener{
        fun onNewsItemClick(url:String)
    }


}