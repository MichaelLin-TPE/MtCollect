package com.collect.collectpeak.fragment.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberBasicData
import com.collect.collectpeak.fragment.member.MemberData
import com.collect.collectpeak.fragment.notice.NoticeType.Companion.REQUEST_FRIEND
import com.collect.collectpeak.fragment.notice.NoticeType.Companion.REQUEST_FRIEND_REJECT
import com.collect.collectpeak.tool.ImageLoaderHandler
import java.util.*
import kotlin.collections.ArrayList

class NoticeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var noticeArray : ArrayList<NoticeData>

    private lateinit var onNoticeItemClickListener: OnNoticeItemClickListener

    fun setOnNoticeItemClickListener(onNoticeItemClickListener: OnNoticeItemClickListener){
        this.onNoticeItemClickListener = onNoticeItemClickListener
    }

    fun setData(noticeArray : ArrayList<NoticeData>){
        this.noticeArray = noticeArray
    }

    override fun getItemViewType(position: Int): Int {

        return noticeArray[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == REQUEST_FRIEND){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.require_friend_item_layouto,parent,false)
            return RequireFriendViewHolder(view)
        }

        if (viewType == REQUEST_FRIEND_REJECT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.require_friend_rejct_item_layouto,parent,false)
            return RequireFriendRejectViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.require_friend_item_layouto,parent,false)
        return RequireFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RequireFriendViewHolder){

            holder.showView(noticeArray[position])
            holder.setOnNoticeItemClickListener(onNoticeItemClickListener)

        }

        if (holder is RequireFriendRejectViewHolder){
            holder.showView(noticeArray[position])
        }
    }

    override fun getItemCount(): Int = noticeArray.size

    class RequireFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private lateinit var onNoticeItemClickListener: OnNoticeItemClickListener

        fun setOnNoticeItemClickListener(onNoticeItemClickListener: OnNoticeItemClickListener){
            this.onNoticeItemClickListener = onNoticeItemClickListener
        }

        private val ivPhoto : ImageView = itemView.findViewById(R.id.notice_photo)

        private val tvContent : TextView = itemView.findViewById(R.id.notice_content)

        private val ivConfirm : ImageView = itemView.findViewById(R.id.notice_confirm)

        private val ivReject : ImageView = itemView.findViewById(R.id.notice_reject)

        fun showView(noticeData: NoticeData) {

            FireStoreHandler.getInstance().getPhotoUrlByUid(noticeData.fromWho,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    ImageLoaderHandler.getInstance().setPhotoUrl(data,ivPhoto)
                }

                override fun onCatchDataFail() {

                }
            })
            FireStoreHandler.getInstance().getUserName(noticeData.fromWho,object :FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    tvContent.text = String.format(Locale.getDefault(),"%s 發送邀請給您",data)
                }

                override fun onCatchDataFail() {

                }

            })

            ivConfirm.setOnClickListener {
                onNoticeItemClickListener.onApplyFriendAccept(noticeData)
            }
            ivReject.setOnClickListener {
                onNoticeItemClickListener.onApplyFriendReject(noticeData)
            }

        }


    }


    class RequireFriendRejectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivPhoto : ImageView = itemView.findViewById(R.id.notice_photo)

        private val tvContent : TextView = itemView.findViewById(R.id.notice_content)

        fun showView(noticeData: NoticeData) {

            FireStoreHandler.getInstance().getPhotoUrlByUid(noticeData.fromWho,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    ImageLoaderHandler.getInstance().setPhotoUrl(data,ivPhoto)
                }

                override fun onCatchDataFail() {

                }
            })
            FireStoreHandler.getInstance().getUserName(noticeData.fromWho,object :FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    tvContent.text = String.format(Locale.getDefault(),"您已拒絕 %s 的邀請.",data)
                }

                override fun onCatchDataFail() {

                }

            })

        }


    }

    interface OnNoticeItemClickListener{
        fun onApplyFriendAccept(data:NoticeData)

        fun onApplyFriendReject(data:NoticeData)
    }

}