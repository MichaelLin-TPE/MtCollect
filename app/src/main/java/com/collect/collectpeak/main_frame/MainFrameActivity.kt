package com.collect.collectpeak.main_frame

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.collect.collectpeak.MtCollectorActivity
import com.collect.collectpeak.R
import com.collect.collectpeak.databinding.ActivityHomeBinding
import com.collect.collectpeak.log.MichaelLog
import com.google.android.material.tabs.TabLayout
import java.util.*

class MainFrameActivity : MtCollectorActivity() {

    companion object {
        const val GO_SELF_PAGE = 0
    }

    private lateinit var dataBinding: ActivityHomeBinding

    private val viewModel: MainFrameViewModel by viewModels {
        val homeRepository = MainFrameRepositoryImpl()
        MainFrameViewModel.HomeViewModelFactory(homeRepository)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        MichaelLog.i("onNewIntent 有收到東西")

        val type = intent?.getIntExtra("type", GO_SELF_PAGE)

        viewModel.onCatchTypeByNewIntent(type)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化 viewModel
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this


        //串切 tabLayout viewPager
        dataBinding.homeTabLayout.addOnTabSelectedListener(
            TabLayout.ViewPagerOnTabSelectedListener(
                dataBinding.homeViewPager
            )
        )
        dataBinding.homeViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                dataBinding.homeTabLayout
            )
        )

        viewModel.onActivityCreate()

        observerFunction()
    }

    override fun onResume() {
        super.onResume()

        viewModel.setOnNewIntentListener(object : MainFrameViewModel.OnNewIntentListener {
            override fun onGoToSelfPage() {
                dataBinding.homeTabLayout.getTabAt(4)?.select()
            }
        })
    }

    private fun observerFunction() {

        // tabLayoutLiveData
        viewModel.tabDataLiveData.observe(this, { tabData ->

            setUpTabLayout(tabData)

        })


        viewModel.viewPagerLiveData.observe(this, { isShowViewPager ->

            if (!isShowViewPager) {
                return@observe
            }
            setUpViewPagerAdapter()

        })

    }

    private fun setUpViewPagerAdapter() {
        val adapter = MainFramePagerAdapter(supportFragmentManager)
        dataBinding.homeViewPager.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPause()
    }

    private fun setUpTabLayout(tabData: TabData) {
        dataBinding.homeTabLayout.removeAllTabs()

        for ((index, title) in tabData.tabTitleArray.withIndex()) {
            val tab = dataBinding.homeTabLayout.newTab()
            tab.customView = setUpTabView(title, tabData.tabIconNotPressArray[index])
            tab.tag = title
            dataBinding.homeTabLayout.addTab(tab)
        }

        //設定第一個TAB 要先被按
        val firstTab = dataBinding.homeTabLayout.getTabAt(0)
        var ivTabIcon = firstTab?.customView?.findViewById<ImageView>(R.id.bottom_tab_icon)
        ivTabIcon?.setImageDrawable(tabData.tabIconPressedArray[0])
        firstTab?.select()
        window.statusBarColor = ContextCompat.getColor(
            this@MainFrameActivity,
            R.color.page_background
        )
        WindowInsetsControllerCompat(window, dataBinding.root).isAppearanceLightStatusBars = true

        dataBinding.homeTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                val singleTab = position?.let { dataBinding.homeTabLayout.getTabAt(it) }
                ivTabIcon = singleTab?.customView?.findViewById(R.id.bottom_tab_icon)
                ivTabIcon?.setImageDrawable(tabData.tabIconPressedArray[position!!])

                window.statusBarColor = if (position == 0) ContextCompat.getColor(
                    this@MainFrameActivity,
                    R.color.page_background
                ) else Color.TRANSPARENT
                WindowInsetsControllerCompat(window, dataBinding.root).isAppearanceLightStatusBars =
                    true
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                val singleTab = position?.let { dataBinding.homeTabLayout.getTabAt(it) }
                ivTabIcon = singleTab?.customView?.findViewById(R.id.bottom_tab_icon)
                ivTabIcon?.setImageDrawable(tabData.tabIconNotPressArray[position!!])
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    /**
     * 設定每個 TAB 的 Icon 與 字串
     */
    private fun setUpTabView(title: String, icon: Drawable): View {
        val view = View.inflate(this, R.layout.home_bottom_tablayout_custom_view, null)
        val tvTitle = view.findViewById<TextView>(R.id.bottom_tab_title)
        val ivIcon = view.findViewById<ImageView>(R.id.bottom_tab_icon)
        tvTitle.text = title
        ivIcon.setImageDrawable(icon)
        return view
    }


}