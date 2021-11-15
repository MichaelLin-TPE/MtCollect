package com.collect.collectpeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.message.MessageFragment
import com.collect.collectpeak.fragment.notice.NoticeFragment
import com.collect.collectpeak.tool.StatusBarTool

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, MessageFragment.newInstance()).commit()

        StatusBarTool.setStatusBarSameColorAsActionBar(window,findViewById(R.id.container))

    }
}