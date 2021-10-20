package com.collect.collectpeak.fragment.member

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.collect.collectpeak.fragment.member.page_fragment.GoalFragment
import com.collect.collectpeak.fragment.member.page_fragment.PostFragment

class MemberViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> PostFragment.newInstance()
            else -> GoalFragment.newInstance()
        }
    }
}