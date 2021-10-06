package com.collect.collectpeak.fragment.profile

import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.fragment.member.MemberData

class ProfileRepositoryImpl : ProfileRepository {


    override fun getUserProfile(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<MemberData>) {
        FireStoreHandler.getInstance().getUserProfile(object : FireStoreHandler.OnFireStoreCatchDataListener<MemberData>{
            override fun onCatchDataSuccess(data: MemberData) {
                onFireStoreCatchDataListener.onCatchDataSuccess(data)
            }

            override fun onCatchDataFail() {
                onFireStoreCatchDataListener.onCatchDataFail()
            }

        })
    }

    override fun editMemberData(
        memberData: MemberData,
        onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<Unit>
    ) {
        FireStoreHandler.getInstance().editMemberData(memberData,object : FireStoreHandler.OnFireStoreCatchDataListener<Unit>{
            override fun onCatchDataSuccess(data: Unit) {
                onFireStoreCatchDataListener.onCatchDataSuccess(Unit)
            }

            override fun onCatchDataFail() {
               onFireStoreCatchDataListener.onCatchDataFail()
            }

        })
    }


}