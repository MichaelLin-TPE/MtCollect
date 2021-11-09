package com.collect.collectpeak.fragment.member

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.collect.collectpeak.fragment.member.page_fragment.goal.GoalFragment
import com.collect.collectpeak.fragment.member.page_fragment.post.PostFragment
import com.collect.collectpeak.log.MichaelLog

class MemberViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(position: Int): Fragment {

        MichaelLog.i("fragment position : $position")

        return when(position){
            0 -> PostFragment.newInstance("")
            else -> GoalFragment.newInstance("uid")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val fragment = super.instantiateItem(container, position)

        return fragment
    }

}