package com.collect.collectpeak.fragment.member.page_fragment.post_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.page_fragment.goal_detail.DetailPhotoAdapter
import com.collect.collectpeak.fragment.share.ShareData
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ImageLoaderHandler
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val POST_LIST = 0;

        //以後做限時動態預留
        const val LIMIT_POST = 1;

        const val GOAL_TYPE = 0

        const val POST_TYPE = 1
    }

    private lateinit var onPostDetailClickListener: OnPostDetailClickListener

    private lateinit var postArray: ArrayList<ShareData>

    fun setOnPostDetailClickListener(onPostDetailClickListener: OnPostDetailClickListener){
        this.onPostDetailClickListener = onPostDetailClickListener
    }

    fun setData(postArray: ArrayList<ShareData>) {
        MichaelLog.i("postArray  ${postArray.size}")
        this.postArray = postArray
    }

    override fun getItemViewType(position: Int): Int {

        return POST_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == POST_LIST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_detail_item_layout, parent, false)
            return PostListViewHolder(view)
        }
        return null!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostListViewHolder){
            MichaelLog.i("showPostItem")
            holder.showView(postArray[position])
            holder.setOnPostDetailClickListener(onPostDetailClickListener)
        }
    }

    override fun getItemCount(): Int = postArray.size


    class PostListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvName : TextView = itemView.findViewById(R.id.post_member_name)

        private val ivMemberPhoto : ImageView = itemView.findViewById(R.id.post_member_photo)

        private val viewPager : ViewPager = itemView.findViewById(R.id.post_view_pager)

        private val ivHeart : ImageView = itemView.findViewById(R.id.post_heart)

        private val ivMessage : ImageView = itemView.findViewById(R.id.post_message)

        private val ivSend : ImageView = itemView.findViewById(R.id.post_send)

        private val tvHeartCount : TextView = itemView.findViewById(R.id.post_heart_count)

        private val tvMessageCount : TextView = itemView.findViewById(R.id.post_message_count)

        private val ivSetting : ImageView = itemView.findViewById(R.id.post_setting)

        private val centerLine : View = itemView.findViewById(R.id.center_line)

        private val tvContent : TextView = itemView.findViewById(R.id.post_content)

        private val photoClick : View = itemView.findViewById(R.id.post_photo_click_area)

        private val settingClick : View = itemView.findViewById(R.id.post_setting_click_are)

        private lateinit var onPostDetailClickListener: OnPostDetailClickListener

        fun setOnPostDetailClickListener(onPostDetailClickListener: OnPostDetailClickListener){
            this.onPostDetailClickListener = onPostDetailClickListener
        }

        fun showView(shareData: ShareData) {

            ivSend.visibility = View.GONE

            //取得名字
            FireStoreHandler.getInstance().getUserName(shareData.uid,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    MichaelLog.i("取得使用者名字 : $data")
                    tvName.text = data
                }
                override fun onCatchDataFail() {
                    MichaelLog.i("無法取得使用者名子")
                }
            })

            FireStoreHandler.getInstance().getUserPhotoUrl(shareData.uid,object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    ImageLoaderHandler.getInstance().setPhotoUrl(data,ivMemberPhoto)
                }

                override fun onCatchDataFail() {
                    MichaelLog.i("無法取得使用者照片")
                }
            })

            val viewPagerAdapter = DetailPhotoAdapter()

            viewPagerAdapter.setData(shareData.photoArray,MtCollectorApplication.getInstance().getContext())

            viewPager.adapter = viewPagerAdapter

            if (shareData.type == GOAL_TYPE){

                val contentArray = shareData.content.split(",")

                val mtName = contentArray[0]

                val level = contentArray[1]

                val timeStamp = contentArray[2].toLong()

                val desc = contentArray[3]

                val content = "${getTime(timeStamp)} 登頂 : $mtName , $level\n$desc"

                tvContent.text = content

            }else{
                tvContent.text = shareData.content
            }


            if (shareData.likeCount != 0){
                tvHeartCount.visibility = View.VISIBLE
                tvHeartCount.text = String.format(Locale.getDefault(),"%d人點讚",shareData.likeCount)
                shareData.likeArray.forEach {
                    if (it.uid == shareData.uid){
                        ivHeart.setImageResource(R.drawable.heart_pressed)
                        return@forEach
                    }
                }
            }else{
                tvHeartCount.visibility = View.GONE
                ivHeart.setImageResource(R.drawable.heart_not_press)
            }
            if (shareData.messageCount != 0){
                centerLine.visibility = View.VISIBLE
                tvMessageCount.visibility = View.VISIBLE
                tvMessageCount.text = String.format(Locale.getDefault(),"%d則留言",shareData.messageCount)
            }

            photoClick.setOnClickListener {
                onPostDetailClickListener.onPhotoClick(shareData.uid)
            }

            settingClick.setOnClickListener {
                onPostDetailClickListener.onSettingClick(shareData)
            }

            ivHeart.setOnClickListener {

                onPostDetailClickListener.onHeartIconClick(shareData)
            }

        }

        private fun getTime(timeStamp: Long): String {

            val sdf = SimpleDateFormat("yyyy/MM/dd",Locale.getDefault())

            return sdf.format(Date(timeStamp))

        }

    }


    interface OnPostDetailClickListener{

        fun onSettingClick(shareData: ShareData)

        fun onHeartIconClick(shareData: ShareData)

        fun onPhotoClick(uid : String)
    }

}