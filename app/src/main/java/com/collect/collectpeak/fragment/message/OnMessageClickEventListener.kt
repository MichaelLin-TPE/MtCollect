package com.collect.collectpeak.fragment.message

interface OnMessageClickEventListener {

    fun onBackPress()

    fun onShowToast(content:String)

    fun goToChatPage(chatId: String)
}