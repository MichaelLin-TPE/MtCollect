package com.collect.collectpeak.fragment.member.page_fragment.goal_edit

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.collect.collectpeak.R
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.ImageLoaderHandler

class GoalEditPhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onPhotoRemoveClickListener: OnPhotoRemoveClickListener

    private lateinit var onAddPhotoClickListener: OnAddPhotoClickListener

    private lateinit var goalEditData: GoalEditData

    fun setData(goalEditData: GoalEditData) {
        this.goalEditData = goalEditData
    }

    companion object{
        const val PHOTO = 0
        const val BITMAP_PHOTO = 2
        const val ADD_PHOTO = 1
    }

    fun setOnAddPhotoClickListener(onAddPhotoClickListener: OnAddPhotoClickListener){
        this.onAddPhotoClickListener = onAddPhotoClickListener
    }

    fun setonPhotoRemoveClickListener(onPhotoRemoveClickListener: OnPhotoRemoveClickListener){
        this.onPhotoRemoveClickListener = onPhotoRemoveClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == PHOTO){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_edit_photo_layout,parent,false)
            return ViewHolder(view)
        }

        if (viewType == BITMAP_PHOTO){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_edit_photo_layout,parent,false)
            return BitmapViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_edit_add_photo_layout,parent,false)

        return AddPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder){

            holder.showView(goalEditData.photoArray[position])
            holder.setOnPhotoRemoveClickListener(onPhotoRemoveClickListener)
            return
        }

        if (holder is AddPhotoViewHolder){
            holder.showView()
            holder.setOnAddPhotoClickListener(onAddPhotoClickListener)
            return
        }

        if (holder is BitmapViewHolder){
            holder.showView(goalEditData.newPhotoArray[position - goalEditData.photoArray.size],position - goalEditData.photoArray.size)
            holder.setOnPhotoRemoveClickListener(onPhotoRemoveClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (position < goalEditData.photoArray.size){
            MichaelLog.i("顯示照片 : $position")
            return PHOTO
        }
        if (position - goalEditData.photoArray.size < goalEditData.newPhotoArray.size){
            MichaelLog.i("顯示Bitmap : $position")
            return BITMAP_PHOTO
        }
        MichaelLog.i("顯示添加照片 : $position")
        return ADD_PHOTO
    }


    override fun getItemCount(): Int = goalEditData.photoArray.size + goalEditData.newPhotoArray.size + 1

    class AddPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private lateinit var onAddPhotoClickListener: OnAddPhotoClickListener

        fun setOnAddPhotoClickListener(onAddPhotoClickListener: OnAddPhotoClickListener) {
            MichaelLog.i("init setOnAddPhotoClickListener")
            this.onAddPhotoClickListener = onAddPhotoClickListener
        }

        private val tvAdd : TextView = itemView.findViewById(R.id.add_photo)

        fun showView(){
            tvAdd.setOnClickListener {
                MichaelLog.i("點擊添加照片")
                onAddPhotoClickListener.onAdd()
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onPhotoRemoveClickListener: OnPhotoRemoveClickListener

        private val ivPhoto : ImageView = itemView.findViewById(R.id.goal_edit_photo)

        private val ivClose : ImageView = itemView.findViewById(R.id.goal_edit_close)

        fun setOnPhotoRemoveClickListener(onPhotoRemoveClickListener: OnPhotoRemoveClickListener){
            MichaelLog.i("init setOnPhotoRemoveClickListener")
            this.onPhotoRemoveClickListener = onPhotoRemoveClickListener
        }


        fun showView(url: String) {

            ImageLoaderHandler.getInstance().setPhotoUrl(url,ivPhoto)

            ivClose.setOnClickListener {
                onPhotoRemoveClickListener.onRemove(url)
            }
        }



    }

    class BitmapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onPhotoRemoveClickListener: OnPhotoRemoveClickListener

        fun setOnPhotoRemoveClickListener(onPhotoRemoveClickListener: OnPhotoRemoveClickListener){
            MichaelLog.i("init setOnPhotoRemoveClickListener")
            this.onPhotoRemoveClickListener = onPhotoRemoveClickListener
        }

        fun showView(bitmap: Bitmap, position: Int) {

            ivPhoto.setImageBitmap(bitmap)

            ivClose.setOnClickListener {
                onPhotoRemoveClickListener.onRemoveBitmap(position)
            }
        }

        private val ivPhoto : ImageView = itemView.findViewById(R.id.goal_edit_photo)

        private val ivClose : ImageView = itemView.findViewById(R.id.goal_edit_close)

    }









    interface OnPhotoRemoveClickListener{
        fun onRemove(photoUrl : String)
        fun onRemoveBitmap(position: Int)
    }

    interface OnAddPhotoClickListener{
        fun onAdd()
    }
}