package com.collect.collectpeak.main_frame

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.collect.collectpeak.fragment.equipment.EquipmentListFragment
import com.collect.collectpeak.fragment.home.HomeFragment
import com.collect.collectpeak.fragment.member.MemberFragment
import com.collect.collectpeak.fragment.mountain.mt_list.MtFragment
import com.collect.collectpeak.fragment.share.ShareFragment

class MainFramePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 5

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment.newInstance()
            1 -> MtFragment.newInstance()
            2 -> EquipmentListFragment.newInstance()
            3 -> ShareFragment.newInstance()
            else -> MemberFragment.newInstance()
        }
    }
}