package com.collect.collectpeak.activity
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.collect.collectpeak.MtCollectorActivity
import com.collect.collectpeak.R
import com.collect.collectpeak.fragment.profile.ProfileFragment

class MemberProfileActivity : MtCollectorActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_profile)

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container,ProfileFragment.newInstance()).commit()

        window.statusBarColor = Color.TRANSPARENT

        val view = findViewById<FrameLayout>(R.id.container)

        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true

    }
}