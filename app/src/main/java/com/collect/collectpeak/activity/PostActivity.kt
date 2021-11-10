package com.collect.collectpeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.member.page_fragment.post_detail.PostDetailFragment
import com.collect.collectpeak.fragment.share.ShareData

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        var data = ShareData()

        var uid = ""

        intent.extras?.let {
            data = it.getParcelable<ShareData>("data") as ShareData
            uid = it.getString("uid","")
        }


        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container,PostDetailFragment.newInstance(data,uid)).commit()

    }
}