package com.collect.collectpeak.fragment.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.AuthHandler
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.firebase.StorageHandler
import com.collect.collectpeak.tool.ImageLoaderHandler
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var messageArray : ArrayList<MessageData>

    companion object{
        const val MINE = 0
        const val OTHER = 1
    }

    fun setData(messageArray :ArrayList<MessageData>){
        this.messageArray = messageArray
    }

    override fun getItemViewType(position: Int): Int {

        return if (messageArray[position].uid == AuthHandler.getCurrentUser()?.uid) MINE else OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == MINE){

            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right_item_layout,parent,false)

            return MineViewHolder(view)

        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left_item_layout,parent,false)
        return OtherViewHolder(view)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MineViewHolder){
            holder.showView(messageArray[position])
        }
        if (holder is OtherViewHolder){
            holder.showView(messageArray[position])
        }

    }

    override fun getItemCount(): Int {
        return messageArray.size
    }


    class MineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tvMessage :TextView = itemView.findViewById(R.id.chat_right_text)

        private val tvTime :TextView = itemView.findViewById(R.id.chat_time)

        fun showView(data : MessageData){
            tvMessage.text = data.message

            tvTime.text = SimpleDateFormat("HH:mma", Locale.getDefault()).format(Date(data.timeStamp))

        }


    }

    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tvMessage :TextView = itemView.findViewById(R.id.chat_right_text)
        private val tvTime :TextView = itemView.findViewById(R.id.chat_time)
        private val ivPhoto : ImageView = itemView.findViewById(R.id.chat_photo)
        fun showView(data : MessageData){
            tvMessage.text = data.message

            tvTime.text = SimpleDateFormat("HH:mma", Locale.getDefault()).format(Date(data.timeStamp))

            FireStoreHandler.getInstance().getPhotoUrlByUid(data.uid,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    ImageLoaderHandler.getInstance().setPhotoUrl(data,ivPhoto)
                }

                override fun onCatchDataFail() {

                }

            })

        }
    }

}