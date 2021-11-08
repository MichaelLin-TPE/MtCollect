package com.collect.collectpeak.fragment.member.page_fragment.post_detail

import com.collect.collectpeak.fragment.share.ShareData

interface OnPostDetailClickEventListener {

    fun onClickSetting(shareData: ShareData);

    fun onShowConfirmDeleteDialog(shareData: ShareData)

    fun onShowProgressDialog()

    fun onDismissProgressDialog()

    fun onGotoPostEditPage(shareData: ShareData)

}