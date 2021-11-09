package com.collect.collectpeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.user_page.UserPageFragment
import com.collect.collectpeak.tool.StatusBarTool

class UserPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)


        val uid = intent.extras?.getString("uid","") ?: return

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container,UserPageFragment.newInstance(uid)).commit()

        StatusBarTool.setStatusBarSameColorAsActionBar(window,findViewById(R.id.container))
    }
}