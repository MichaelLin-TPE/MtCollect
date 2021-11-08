package com.collect.collectpeak.fragment.member.page_fragment.post_edit

interface OnPostEditClickEventListener {

    fun onShowProgressDialog()

    fun onShowPictureSelector(picCount : Int)

    fun onDismissProgressDialog()

    fun onShowToast(content:String)

    fun onFinishPage()
}