package com.collect.collectpeak.fragment.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ImageLoaderHandler
import java.lang.invoke.ConstantCallSite

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private lateinit var messageArray : ArrayList<MessageListData>

    private lateinit var onMessageListItemClickListener: OnMessageListItemClickListener

    fun setOnMessageListItemClickListener(onMessageListItemClickListener: OnMessageListItemClickListener){
        this.onMessageListItemClickListener = onMessageListItemClickListener
    }

   fun setData(messageArray : ArrayList<MessageListData>){
       this.messageArray = messageArray
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_list_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val messageData = messageArray[position]

        FireStoreHandler.getInstance().getPhotoUrlByUid(messageData.uid,object :FireStoreHandler.OnFireStoreCatchDataListener<String>{
            override fun onCatchDataSuccess(data: String) {
                ImageLoaderHandler.getInstance().setPhotoUrl(data,holder.ivPhoto)
            }

            override fun onCatchDataFail() {
                MichaelLog.i("無法取得照片")
            }

        })


        FireStoreHandler.getInstance().getUserProfileByUid(messageData.uid,object : FireStoreHandler.OnFireStoreCatchDataListener<MemberData>{
            override fun onCatchDataSuccess(data: MemberData) {
                holder.tvName.text = if (data.name.isEmpty()) data.email else data.name
            }

            override fun onCatchDataFail() {
                MichaelLog.i("無法取得名字")
            }

        })

        holder.tvContent.text = messageData.lastMessage


        holder.root.setOnClickListener {
            onMessageListItemClickListener.onMessageClick(messageData)
        }

    }

    override fun getItemCount(): Int = messageArray.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivPhoto : ImageView = itemView.findViewById(R.id.message_photo)

        val tvName : TextView = itemView.findViewById(R.id.message_name)

        val tvContent : TextView = itemView.findViewById(R.id.message_msg)

        val root : ConstraintLayout = itemView.findViewById(R.id.message_root)

    }

    fun interface OnMessageListItemClickListener{
        fun onMessageClick(data : MessageListData)
    }
}