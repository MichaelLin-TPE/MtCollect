package com.collect.collectpeak.fragment.equipment.equipment_select

interface OnEquipmentSelectClickEventListener {


    fun onShowProgress(content :String)

    fun onDismissProgressDialog()

    fun onShowToast(content: String)

    fun onFinishPage()


}