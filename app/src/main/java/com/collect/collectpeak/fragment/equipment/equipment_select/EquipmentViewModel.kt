package com.collect.collectpeak.fragment.equipment.equipment_select

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.collect.collectpeak.MtCollectorApplication
import com.collect.collectpeak.R
import com.collect.collectpeak.firebase.FireStoreHandler
import com.collect.collectpeak.log.MichaelLog
import com.collect.collectpeak.tool.TimeTool
import com.google.gson.Gson
import java.util.ArrayList

class EquipmentViewModel(private val equipmentRepository: EquipmentRepository) : ViewModel() {


    val equipmentListLiveData = MutableLiveData<EquipmentListData>()

    val progressBarLiveData = MutableLiveData(View.VISIBLE)

    private var targetEquipmentData = EquipmentListData()

    private var equipmentListTitle = TimeTool.getCurrentDate() + " ${
        MtCollectorApplication.getInstance().getContext().getString(
            R.string.equipment_select_list
        )
    }"

    fun onFragmentStart() {

        equipmentRepository.getEquipmentList(object :
            FireStoreHandler.OnFireStoreCatchDataListener<EquipmentListData> {
            override fun onCatchDataSuccess(data: EquipmentListData) {

                progressBarLiveData.value = View.GONE

                MichaelLog.i("最終的資料：${Gson().toJson(data)}")
                targetEquipmentData = data
                equipmentListLiveData.value = data


            }

            override fun onCatchDataFail() {

            }

        })
    }

    fun onEquipmentItemCheckListener(data: EquipmentData) {
        MichaelLog.i("被勾選的裝備：${data.name}")

        targetEquipmentData.equipmentList.forEach {
            updateEquipmentList(it, data)
        }

        equipmentListLiveData.value = targetEquipmentData

    }

    private fun updateEquipmentList(list: ArrayList<EquipmentData>, data: EquipmentData) {

        run lit@{
            list.forEach {
                if (it.name == data.name) {
                    it.isCheck = data.isCheck
                    return@lit
                }
            }
        }

    }

    fun onEditNameChangeListener(equipmentListTitle: String) {
        this.equipmentListTitle = equipmentListTitle
    }

    fun onButtonDoneClickListener() {

        //等有使用者了這邊才會開始做

    }


    open class EquipmentViewModelFactory(private val equipmentRepository: EquipmentRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EquipmentViewModel(equipmentRepository) as T
        }
    }

}