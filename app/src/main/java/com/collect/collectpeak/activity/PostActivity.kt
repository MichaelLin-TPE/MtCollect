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

        val data = intent.extras?.getParcelable<ShareData>("data") as ShareData

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container,PostDetailFragment.newInstance(data)).commit()

    }
}