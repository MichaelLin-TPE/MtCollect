package com.collect.collectpeak.tool
interface OnButtonCallBackListener<T> {

    fun onCatchResult(result :T)

    fun showLoadingDialog(result: T)

    fun dismissLoadingDialog()

    fun showToast(result: T)

    fun finishPage()

}