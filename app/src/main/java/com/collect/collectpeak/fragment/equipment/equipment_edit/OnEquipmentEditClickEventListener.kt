package com.collect.collectpeak.fragment.equipment.equipment_edit

interface OnEquipmentEditClickEventListener {


    fun onShowProgress(content :String)

    fun onDismissProgressDialog()

    fun onShowToast(content: String)

    fun onFinishPage()

    fun onShowEditNameDialog()

    fun onGoToEditEquipmentListPage()

}