package com.collect.collectpeak.fragment.mountain.peak_photo

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.collect.collectpeak.R
import com.makeramen.roundedimageview.RoundedImageView
import java.lang.UnsupportedOperationException
import java.util.*

class PhotoAdapter : PagerAdapter() {


    private lateinit var bitmapArray : ArrayList<Bitmap>

    private lateinit var context: Context

    fun setData(bitmapArray: ArrayList<Bitmap>, context: Context){
        this.bitmapArray = bitmapArray
        this.context = context
    }


    override fun getCount(): Int = bitmapArray.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_view_pager_item, null)
        val ivPhoto: RoundedImageView = view.findViewById(R.id.dialog_view_pager_photo)

        val tvCount = view.findViewById<TextView>(R.id.dialog_view_pager_pic_count)
        tvCount.text =
            String.format(Locale.getDefault(), "%d/%d", position + 1, bitmapArray.size)

        ivPhoto.setImageBitmap(bitmapArray[position])
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}