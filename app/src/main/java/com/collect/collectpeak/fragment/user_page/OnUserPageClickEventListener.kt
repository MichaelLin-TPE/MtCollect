package com.collect.collectpeak.fragment.user_page

interface OnUserPageClickEventListener {

    fun onBackClick()

    fun onShowProgressDialog()

    fun onDismissProgressDialog()

    fun onShowToast(content:String)

    fun onGoToUserChatPage(targetChatId: String)
}