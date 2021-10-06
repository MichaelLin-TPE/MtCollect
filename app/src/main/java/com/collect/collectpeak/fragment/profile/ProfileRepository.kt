package com.collect.collectpeak.fragment.profile

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberData

interface ProfileRepository {
    fun getUserProfile(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<MemberData>)
    fun editMemberData(memberData: MemberData, onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<Unit>)
}