package com.collect.collectpeak.fragment.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.page_fragment.PostFragment
import com.collect.collectpeak.fragment.member.page_fragment.goal.GoalFragment
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.FragmentUtil
import com.collect.collectpeak.tool.ImageLoaderHandler
import com.collect.collectpeak.tool.Tool
import com.google.android.material.tabs.TabLayout
import java.lang.reflect.Member

class MemberAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MEMBER_INFO = 0
        const val MEMBER_TYPE_PAGE = 1
    }

    private lateinit var onMemberInfoClickListener: OnMemberInfoClickListener

    private lateinit var fragmentManager: FragmentManager


    fun setOnMemberInfoClickListener(onMemberInfoClickListener: OnMemberInfoClickListener) {
        this.onMemberInfoClickListener = onMemberInfoClickListener
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == MEMBER_INFO) {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.member_info_layout, parent, false)
            return MemberInfoViewHolder(view)

        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.member_type_page_layout, parent, false)


        return MemberPageViewHolder(view)
//        return MemberTypePageViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MemberInfoViewHolder) {

            holder.showView()
            holder.setOnMemberInfoClickListener(onMemberInfoClickListener)
            return
        }
        if (holder is MemberPageViewHolder){
            holder.showView(fragmentManager)

        }

        if (holder is MemberTypePageViewHolder) {
            holder.showView(fragmentManager)
        }


    }

    override fun getItemCount(): Int = 2

    override fun getItemViewType(position: Int): Int {

        if (position == 0) {
            return MEMBER_INFO
        }
        if (position == 1) {
            return MEMBER_TYPE_PAGE
        }

        return MEMBER_TYPE_PAGE
    }

    class MemberInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var onMemberInfoClickListener: OnMemberInfoClickListener

        private val ivPhoto = itemView.findViewById<ImageView>(R.id.member_photo)

        private val tvPostCount = itemView.findViewById<TextView>(R.id.member_post_count)

        private val tvGoalCount = itemView.findViewById<TextView>(R.id.member_mountain_count)

        private val tvFriendCount = itemView.findViewById<TextView>(R.id.member_friend_count)

        private val editView = itemView.findViewById<ConstraintLayout>(R.id.member_edit_profile)

        private val tvName = itemView.findViewById<TextView>(R.id.member_post_name)

        private val tvDescription = itemView.findViewById<TextView>(R.id.member_post_description)

        fun showView() {

            ivPhoto.setOnClickListener {
                onMemberInfoClickListener.onPhotoSelectListener()
            }

            editView.setOnClickListener {
                onMemberInfoClickListener.onEditUserProfileClickListener()
            }

            FireStoreHandler.getInstance().getUserProfile(object : FireStoreHandler.OnFireStoreCatchDataListener<MemberData>{
                override fun onCatchDataSuccess(data: MemberData) {
                    tvDescription.text = data.description
                    tvName.text = data.name
                }

                override fun onCatchDataFail() {
                    MichaelLog.i("無法取得使用者資料")
                }

            })



            FireStoreHandler.getInstance().getPhotoUrl(object : FireStoreHandler.OnFireStoreCatchDataListener<String>{
                override fun onCatchDataSuccess(data: String) {
                    MichaelLog.i("取得網址成功")
                    if (data.isEmpty()){
                        return
                    }
                    ImageLoaderHandler.getInstance().setPhotoUrl(data,ivPhoto)
                }

                override fun onCatchDataFail() {

                    MichaelLog.i("取得照片網址失敗")

                }
            })

            FireStoreHandler.getInstance().getMemberBasicData(object : FireStoreHandler.OnFireStoreCatchDataListener<MemberBasicData>{
                override fun onCatchDataSuccess(data: MemberBasicData) {

                    tvFriendCount.text = "朋友\n"
                    tvPostCount.text = "貼文\n"
                    tvGoalCount.text = "登頂\n"

                    tvFriendCount.append(data.friendCount.toString())
                    tvGoalCount.append(data.goalCount.toString())
                    tvPostCount.append(data.postCount.toString())
                }

                override fun onCatchDataFail() {
                    MichaelLog.i("取得基本資料失敗失敗")
                }
            })




        }

        fun setOnMemberInfoClickListener(onMemberInfoClickListener: MemberAdapter.OnMemberInfoClickListener) {
            this.onMemberInfoClickListener = onMemberInfoClickListener
        }

    }

    class MemberPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val postView : ConstraintLayout = itemView.findViewById(R.id.post_view)

        private val goalView : ConstraintLayout = itemView.findViewById(R.id.goal_view)

        private val ivPost : ImageView = itemView.findViewById(R.id.post_icon)

        private val ivGoal : ImageView = itemView.findViewById(R.id.goal_icon)

        private val postLine : View = itemView.findViewById(R.id.post_line)

        private val goalLine : View = itemView.findViewById(R.id.goal_line)

        private val root : ConstraintLayout = itemView.findViewById(R.id.member_page_root)

        fun showView(fragmentManager: FragmentManager) {



            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.member_container,PostFragment.newInstance())
            transaction.commit()

            postView.setOnClickListener{
                val postTransaction = fragmentManager.beginTransaction()
                ivPost.setImageResource(R.drawable.post_pressed)
                ivGoal.setImageResource(R.drawable.goal_not_press)
                postLine.visibility = View.VISIBLE
                goalLine.visibility = View.GONE
                postTransaction.replace(R.id.member_container,PostFragment.newInstance())
                postTransaction.commit()
            }
            goalView.setOnClickListener {
                val goalTransaction = fragmentManager.beginTransaction()
                ivPost.setImageResource(R.drawable.post_not_press)
                ivGoal.setImageResource(R.drawable.goal_pressed)
                postLine.visibility = View.GONE
                goalLine.visibility = View.VISIBLE
                goalTransaction.replace(R.id.member_container,GoalFragment.newInstance())
                goalTransaction.commit()
            }

        }

    }


    class MemberTypePageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewPager = itemView.findViewById<ViewPager>(R.id.member_type_view_pager)

        private val tabLayout = itemView.findViewById<TabLayout>(R.id.member_type_tab)

        fun showView(fragmentManager: FragmentManager) {

            val tabNotPressArray = ArrayList<Int>()
            tabNotPressArray.add(R.drawable.post_not_press)
            tabNotPressArray.add(R.drawable.goal_not_press)

            val tabPressedArray = ArrayList<Int>()

            tabPressedArray.add(R.drawable.post_pressed)
            tabPressedArray.add(R.drawable.goal_pressed)

            setUpTab(tabNotPressArray)

            setUpViewPager(fragmentManager)

            tabLayout.addOnTabSelectedListener(
                TabLayout.ViewPagerOnTabSelectedListener(
                    viewPager
                )
            )
            viewPager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(
                    tabLayout
                )
            )

            val firstTab = tabLayout.getTabAt(0)
            var ivTabIcon = firstTab?.customView?.findViewById<ImageView>(R.id.member_tab_icon)
            ivTabIcon?.setImageResource(tabPressedArray[0])
            firstTab?.select()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position
                    val singleTab = position?.let { tabLayout.getTabAt(position) }
                    ivTabIcon = singleTab?.customView?.findViewById(R.id.member_tab_icon)
                    ivTabIcon?.setImageResource(tabPressedArray[position!!])
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                    val position = tab?.position
                    val singleTab = position?.let { tabLayout.getTabAt(it) }
                    ivTabIcon = singleTab?.customView?.findViewById(R.id.member_tab_icon)
                    ivTabIcon?.setImageResource(tabNotPressArray[position!!])

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })


        }

        private fun setUpViewPager(fragmentManager: FragmentManager) {
            val adapter = MemberViewPagerAdapter(fragmentManager)
            viewPager.adapter = adapter
        }

        private fun setUpTab(tabNotPressArray: ArrayList<Int>) {
            tabLayout.removeAllTabs()

            for (id in tabNotPressArray) {
                val tab = tabLayout.newTab()
                tab.customView = setUpTabView(id)
                tab.tag = id
                tabLayout.addTab(tab)
            }

        }

        private fun setUpTabView(id: Int): View {

            val view = View.inflate(
                MtCollectorApplication.getInstance().getContext(),
                R.layout.member_tab_item_layout,
                null
            )
            val ivIcon = view.findViewById<ImageView>(R.id.member_tab_icon)
            ivIcon.setImageResource(id)
            return view
        }

    }

    interface OnMemberInfoClickListener {
        fun onPhotoSelectListener()

        fun onEditUserProfileClickListener()
    }

}